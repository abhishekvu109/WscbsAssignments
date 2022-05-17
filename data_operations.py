import pandas as pd
pd.options.mode.chained_assignment = None
import numpy as np
import matplotlib.pyplot as plt
import re
from textblob import TextBlob
from wordcloud import WordCloud
import plotly.express as px
import glob
import os


def prepare_data():
    file_dir = './archive'
    files = glob.glob(os.path.join(file_dir, "*.csv"))
    # Make a list of dataframes while adding a stick_ticker column
    dataframes = [pd.read_csv(file, encoding='latin1').assign(vaccine_file=os.path.basename(file).strip(".csv")).dropna() for
                  file in files]
    # Concatenate all the dataframes into one
    df = pd.concat(dataframes, ignore_index=True)
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
    return df


def clean_tweet_text(text):
    text = re.sub(r'@\w+', '', text)
    text = re.sub(r'#', '', text)
    text = re.sub(r'RT[\s]+', '', text)
    text = re.sub(r'https?:\/\/\S+', '', text)
    text = text.lower()
    return text


def print_df(df):
    print(df)