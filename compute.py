#!/usr/bin/env python3
import os, glob
from pathlib import Path
import re
from textblob import TextBlob
import plotly.express as px
# python -m textblob.download_corpora
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from wordcloud import WordCloud
import wordninja
from spellchecker import SpellChecker
from collections import Counter
import nltk
import math
import random
import shutil

nltk.download('wordnet')
nltk.download('stopwords')
nltk.download('omw-1.4')
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords


## @package compute
#  This module is used for computing the information for provided vaccine name.
#  The computation consists of data-processing using pandas, generating time-series analysis and word clouds for
#  input vaccine name.


stop_words = set(stopwords.words('english'))
stop_words.add("amp")
pd.options.mode.chained_assignment = None
all_vax = ['covaxin', 'sinopharm', 'sinovac', 'moderna', 'pfizer', 'biontech', 'oxford', 'astrazeneca', 'sputnik']
tweet_dir = 'data/tweets'
images_dir = 'data/images'


def vaccine_data_processing(vaccine, input_file):
    ##
    # @brief Perform data-processing on provided input-file and generate information about the supplied vaccine.
    #
    # @param vaccine vaccine-name supplied
    # @param input_file input-file having data-set consisting of tweets about COVID-19 vaccines
    #
    df = pd.read_csv(input_file, encoding='latin1', quotechar='"', delimiter=',')
    # data pre-processing and cleaning
    df.drop(columns=['id', 'tweetid', 'guid', 'link', 'source', 'lang',
                     'quoted_text', 'tweet_type', 'in_reply_to_screen_name',
                     'in_reply_to_user_id', 'in_reply_to_status_id', 'retweeted_screen_name',
                     'retweeted_user_id', 'retweeted_status_id', 'user_id',
                     'profile_image_url', 'user_statuses_count', 'user_friends_count',
                     'user_followers_count', 'user_created_at', 'user_bio', 'user_location',
                     'user_verified', 'Unnamed: 29'], inplace=True)
    df = df.drop_duplicates('description')
    df['description'].transform(clean_tweet_text)
    df['date'] = pd.to_datetime(df['pubdate']).dt.date
    # applying the TextBlob API onto our tweet data to perform sentiment analysis
    df['polarity'] = df['description'].apply(lambda x: TextBlob(x).sentiment.polarity)
    df['subjectivity'] = df['description'].apply(lambda x: TextBlob(x).sentiment.subjectivity)
    # polarity values ranging from -1 to 1 are used for sentiment analysis
    # converting our data to 3 classes (negative, neutral, and positive) so that we can visualize it
    criteria = [df['polarity'].between(-1, -0.01), df['polarity'].between(-0.01, 0.01), df['polarity'].between(0.01, 1)]
    values = ['negative', 'neutral', 'positive']
    df['sentiment'] = np.select(criteria, values, 0)
    vaccine_df, vaccine_timeline = filter_by_vaccy(df, [vaccine])
    vaccine_df.sort_values(by='polarity', ascending=True)[['description', 'pubdate', 'polarity']].reset_index(
        drop=True).head(n=10).to_json(tweet_dir + '/' + vaccine + r'_10_most_negative_tweets.json')
    vaccine_df.sort_values(by='polarity', ascending=False)[['description', 'pubdate', 'polarity']].reset_index(
        drop=True).head(n=10).to_json(tweet_dir + '/' + vaccine + r'_10_most_positive_tweets.json')
    fig = px.bar(vaccine_timeline, x='date', y='count', color='polarity')
    fig.show()
    # fig.write_image(images_dir + '/' + vaccine+"_timeseries_polarity_optimised_dataset.png")
    wordcloud_df = vaccine_df
    wordcloud_df['words'] = vaccine_df.description.apply(lambda x: re.findall(r'\w+', x))
    wordcloud_fig = get_smart_clouds(wordcloud_df)
    if wordcloud_fig is None:
        src = './data/image_na.png'
        dst = images_dir + '/' + vaccine + "_sentiment_wordclouds_optimised_dataset.png"
        shutil.copy(src, dst)
    else:
        wordcloud_fig.savefig(images_dir + '/' + vaccine + "_sentiment_wordclouds_optimised_dataset.png",
                              bbox_inches="tight")


# use regular expressions to strip each tweet of mentions, hashtags, retweet information, and links
def clean_tweet_text(text):
    ##
    # @brief Perform cleansing of tweets from the provided input-file to strip unnecessary elements
    # (mentions, hashtags, retweet information, and links)
    #
    # @param text sentence(s) to be sanitised
    #
    text = re.sub(r'@\w+', '', text)
    text = re.sub(r'#', '', text)
    text = re.sub(r'RT[\s]+', '', text)
    text = re.sub(r'https?:\/\/\S+', '', text)
    text = re.sub(r'http?:\/\/\S+', '', text)
    text = text.lower()
    return text


# Function to filter the data to a single vaccine and plot the timeline Note: a lot of the tweets seem to contain
# hashtags for multiple vaccines even though they are specifically referring to one vaccine-- not very helpful!
def filter_by_vaccy(df, vax):
    ##
    # @brief Filter the tweets and other info from data-frame per vaccine
    # values-included: ['date', 'count', 'polarity', 'retweet_count', 'favorite_count', 'subjectivity']
    #
    # @param df data-frame
    # @param vax input vaccine-name
    #
    df_filt = pd.DataFrame()
    for v in vax:
        df_filt = df_filt.append(df[df['description'].str.lower().str.contains(v)])
    other_vax = list(set(all_vax) - set(vax))
    for o in other_vax:
        df_filt = df_filt[~df_filt['description'].str.lower().str.contains(o)]
    #     df_filt = df_filt.drop_duplicates()
    timeline = df_filt.groupby(['date']).agg(np.nanmean).reset_index()
    timeline['count'] = df_filt.groupby(['date']).count().reset_index()['retweet_count']
    timeline = timeline[['date', 'count', 'polarity', 'retweet_count', 'favorite_count', 'subjectivity']]
    timeline["polarity"] = timeline["polarity"].astype(float)
    timeline["subjectivity"] = timeline["subjectivity"].astype(float)
    return df_filt, timeline


# Advanced word-cloud (positive, negative and neutral separation)
def flatten_list(l):
    ##
    # @brief Flattens the list
    #
    # @param l list to be flattened
    #
    return [x for y in l for x in y]


def is_acceptable(word: str):
    ##
    # @brief Check if the input word is not a stopword (e.g., a, an, the), and hence is acceptable
    #
    # @param word input-word
    #
    return word not in stop_words and len(word) > 2


# Color coding our wordclouds
def red_color_func(word, font_size, position, orientation, random_state=None, **kwargs):
    ##
    # @brief set HSL (Hue Lightness Saturation) model as red
    #
    return f"hsl(0, 100%, {random.randint(25, 75)}%)"


def green_color_func(word, font_size, position, orientation, random_state=None, **kwargs):
    ##
    # @brief set HSL (Hue Lightness Saturation) model as green
    #
    return f"hsl({random.randint(90, 150)}, 100%, 30%)"


def yellow_color_func(word, font_size, position, orientation, random_state=None, **kwargs):
    ##
    # @brief set HSL (Hue Lightness Saturation) model as yellow
    #
    return f"hsl(42, 100%, {random.randint(25, 50)}%)"


# Reusable function to generate word clouds
def generate_word_clouds(neg_doc, neu_doc, pos_doc):
    ##
    # @brief generate word cloud based on the supplied list of negative words, neutral words and positive words
    # @param neg_doc consists of negative words
    # @param neu_doc consists of neutral words
    # @param pos_doc consists of positive words
    # Display the generated image:

    fig, axes = plt.subplots(1, 3, figsize=(20, 10))

    wordcloud_neg = WordCloud(max_font_size=50, max_words=100, background_color="white").generate(" ".join(neg_doc))
    axes[0].imshow(wordcloud_neg.recolor(color_func=red_color_func, random_state=3), interpolation='bilinear')
    axes[0].set_title("Negative Words")
    axes[0].axis("off")

    wordcloud_neu = WordCloud(max_font_size=50, max_words=100, background_color="white").generate(" ".join(neu_doc))
    axes[1].imshow(wordcloud_neu.recolor(color_func=yellow_color_func, random_state=3), interpolation='bilinear')
    axes[1].set_title("Neutral Words")
    axes[1].axis("off")

    wordcloud_pos = WordCloud(max_font_size=50, max_words=100, background_color="white").generate(" ".join(pos_doc))
    axes[2].imshow(wordcloud_pos.recolor(color_func=green_color_func, random_state=3), interpolation='bilinear')
    axes[2].set_title("Positive Words")
    axes[2].axis("off")

    plt.tight_layout()
    #     plt.show();
    return fig

# Returns a list of "top-n" most frequent words in a list
def get_top_percent_words(doc, percent):
    ##
    # @brief get top N percentage of words which are most frequent
    #
    # @param doc document from which the top N percent of frequent words are to be identified
    # @param N top N percent frequent words to be identified
    #
    top_n = int(percent * len(set(doc)))
    counter = Counter(doc).most_common(top_n)
    top_n_words = [x[0] for x in counter]
    # print(top_n_words)
    return top_n_words


def clean_document(doc):
    ##
    # @brief Performs the cleansing of data by removing stop words and lemmatizing the words
    #
    # @param doc document to be cleaned
    #
    spell = SpellChecker()
    lemmatizer = WordNetLemmatizer()

    # Lemmatize words (needed for calculating frequencies correctly )
    doc = [lemmatizer.lemmatize(x) for x in doc]

    # Get the top 10% of all words. This may include "misspelled" words
    top_n_words = get_top_percent_words(doc, 0.1)

    # Get a list of misspelled words
    misspelled = spell.unknown(doc)

    # Accept the correctly spelled words and top_n words
    clean_words = [x for x in doc if x not in misspelled or x in top_n_words]

    # Try to split the misspelled words to generate good words (ex. "lifeisstrange" -> ["life", "is", "strange"])
    words_to_split = [x for x in doc if x in misspelled and x not in top_n_words]
    split_words = flatten_list([wordninja.split(x) for x in words_to_split])

    # Some splits may be nonsensical, so reject them ("llouis" -> ['ll', 'ou', "is"])
    clean_words.extend(spell.known(split_words))

    return clean_words

# Provides a measure of how well a particular model fits the data;
def get_log_likelihood(doc1, doc2):
    ##
    # @brief Provides a measure of how well a particular model fits the data;
    #
    # @param doc1 list to be compared another list
    # @param doc2 list compared with
    #
    doc1_counts = Counter(doc1)
    doc1_freq = {
        x: doc1_counts[x] / len(doc1)
        for x in doc1_counts
    }

    doc2_counts = Counter(doc2)
    doc2_freq = {
        x: doc2_counts[x] / len(doc2)
        for x in doc2_counts
    }

    doc_ratios = {
        # 1 is added to prevent division by 0
        x: math.log((doc1_freq[x] + 1) / (doc2_freq[x] + 1))
        for x in doc1_freq if x in doc2_freq
    }

    top_ratios = Counter(doc_ratios).most_common()
    top_percent = int(0.1 * len(top_ratios))
    return top_ratios[:top_percent]


# Function to generate a document based on likelihood values for words
def get_scaled_list(log_list):
    ##
    # @brief Function to generate a document based on likelihood values for words
    #
    # @param log_list list consisting of words and likelihood values
    #
    counts = [int(x[1] * 100000) for x in log_list]
    words = [x[0] for x in log_list]
    cloud = []
    for i, word in enumerate(words):
        cloud.extend([word] * counts[i])
    # Shuffle to make it more "real"
    random.shuffle(cloud)
    return cloud


def get_smart_clouds(df):
    ##
    # @brief Retrieve smart cloud from the data-frame by splitting it into positive, neutral and negative words
    # from the dataset.
    #
    # @param df data-frame supplied
    #
    neg_doc = flatten_list(df[df['sentiment'] == 'negative']['words'])
    neg_doc = [x for x in neg_doc if is_acceptable(x)]

    pos_doc = flatten_list(df[df['sentiment'] == 'positive']['words'])
    pos_doc = [x for x in pos_doc if is_acceptable(x)]

    neu_doc = flatten_list(df[df['sentiment'] == 'neutral']['words'])
    neu_doc = [x for x in neu_doc if is_acceptable(x)]

    # Clean all the documents
    neg_doc_clean = clean_document(neg_doc)
    neu_doc_clean = clean_document(neu_doc)
    pos_doc_clean = clean_document(pos_doc)

    # Combine classes B and C to compare against A (ex. "positive" vs "non-positive")
    top_neg_words = get_log_likelihood(neg_doc_clean, flatten_list([pos_doc_clean, neu_doc_clean]))
    top_neu_words = get_log_likelihood(neu_doc_clean, flatten_list([pos_doc_clean, neg_doc_clean]))
    top_pos_words = get_log_likelihood(pos_doc_clean, flatten_list([neu_doc_clean, neg_doc_clean]))

    # Generate syntetic a corpus using our loglikelihood values
    neg_doc_final = get_scaled_list(top_neg_words)
    neu_doc_final = get_scaled_list(top_neu_words)
    pos_doc_final = get_scaled_list(top_pos_words)

    # Visualise our synthetic corpus
    if not neg_doc_final and not neu_doc_final and not pos_doc_final:
        return None
    else:
        fig = generate_word_clouds(neg_doc_final, neu_doc_final, pos_doc_final)
        return fig


def validate_vaccine_name(passed_vaccine):
    ##
    # @brief Validates the input vaccine parameter against pre-defined accepted vaccines.
    #
    # @param passed_vaccine vaccine-name supplied
    #
    all_vax = ['covaxin', 'sinopharm', 'sinovac', 'moderna', 'pfizer', 'biontech', 'oxford', 'astrazeneca', 'sputnik',
               'dummy']
    return passed_vaccine in all_vax


# Accept the vaccine name as input, validate whether it is appropriate, and
# generate top tweets, time-series and word cloud for the supplied vaccine.
if __name__ == '__main__':
    Path('./' + images_dir).mkdir(parents=True, exist_ok=True)
    Path('./' + tweet_dir).mkdir(parents=True, exist_ok=True)
    input_file = input("Please provide the absolute path of .csv containing tweets on which you'd like analysis: \n")
    vaccine = input("Please enter vaccine name for which you want the sentiment analysis computation: \n")
    if validate_vaccine_name(vaccine.lower()):
        vaccine_data_processing(vaccine.lower(), input_file)
    else:
        print("Please run the program again and enter a valid vaccine name for which you want the sentiment analysis "
              "visualisation!\n")
