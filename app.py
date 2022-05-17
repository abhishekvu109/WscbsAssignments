import json
from flask import Flask, jsonify, request, make_response
import re
import base64
from pathlib import Path
from pydblite import Base
import data_operations as dops
import logging

app = Flask(__name__)
logging.basicConfig(filename='flask_console.log', level=logging.DEBUG,
                    format='%(asctime)s %(levelname)s %(name)s: %(message)s')
db = Base('covidvaccinetweetanalysis.pdl', save_to_file=True)

if db.exists():
    db.open()
else:
    # db.create('id', 'long_url', 'short_url', 'user', 'expiry_time')
    # db.create_index('id')
    pass


@app.route('/')
def hello_world():  # put application's code here
    # df=dops.prepare_data()
    app.logger.info("Data successfully prepared!")
    # dops.print_df(df)
    return 'Hello World!'


@app.route("/timeseries/<string:vaccine>", methods=['GET'])
def time_series_analysis(vaccine):
    value = ''
    base64_encoded_value = ''
    match vaccine.lower():
        case 'covaxin' | 'moderna' | 'sputnik' | 'sinopharm' | 'sinovac':
            value = vaccine.lower()
        case 'pfizer' | 'biontech':
            value = 'pfizer'
        case 'oxford' | 'astrazeneca':
            value = 'astrazeneca'
        case _:
            value = ''
    file = Path("./images/" + value + "_timeseries_analysis_revised_dataset.png")
    if file.is_file():
        with open(file, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        response = make_response(jsonify(
            {"value": encoded_string.decode('utf-8')}), 200, )
        app.logger.info("Timeseries analysis image successfully encoded for vaccine " + vaccine.lower() + "!")
    else:
        response = make_response(jsonify(
            {"value": ''}), 404, )  # for null-case when there is no wordcloud file for that vaccine
        app.logger.info(
            "Timeseries analysis image not available for vaccine " + vaccine.lower() + "! Need to compute for image generation...")
    response.headers["Content-Type"] = "application/json"
    return response


@app.route("/tweets/<string:vaccine>", methods=['GET'])
def get_negative_neutral_positive_tweets(vaccine):
    value = ''
    match vaccine.lower():
        case 'covaxin' | 'moderna' | 'sputnik' | 'sinopharm' | 'sinovac':
            value = vaccine.lower()
        case 'pfizer' | 'biontech':
            value = 'pfizer'
        case 'oxford' | 'astrazeneca':
            value = 'astrazeneca'
        case _:
            value = ''
    if value:
        response = make_response(custom_json_parsing(vaccine), 200, )
        app.logger.info("Positive and negative tweets available for vaccine  " + vaccine.lower() + "!")
    else:
        response = make_response(jsonify(
            {"value": ''}), 404, )  # for null-case when there is no wordcloud file for that vaccine
        app.logger.info(
            "Positive and negative tweets not available for vaccine " + vaccine.lower() + "! Need to compute tweet JSON files...")
    response.headers["Content-Type"] = "application/json"
    return response


def custom_json_parsing(vaccine):
    store_negative_list = []
    store_positive_list = []
    # for negative tweets
    file = Path('./tweets/'+vaccine+'_10_most_negative_tweets.json')
    if file.is_file():
        input_negative_file = open('./tweets/'+vaccine+'_10_most_negative_tweets.json')
        json_array = json.load(input_negative_file)
        tweet_array = []
        pubdate_array = []
        polarity_array = []
        # print(json_array)

        for key, value in json_array.items():
            if key == 'description':
                tweet_array = (list(value.values()))
            if key == 'pubdate':
                pubdate_array = list(value.values())
            if key == 'polarity':
                polarity_array = list(value.values())
        for iterator in range(len(tweet_array)):
            store_details = {"tweet": None, "date": None, "polarity": None}
            store_details["tweet"] = tweet_array[iterator]
            store_details["date"] = pubdate_array[iterator]
            store_details["polarity"] = polarity_array[iterator]
            store_negative_list.append(store_details)

    # print(store_negative_list)
    # for positive tweets
    file = Path('./tweets/' + vaccine + '_10_most_positive_tweets.json')
    if file.is_file():
        input_positive_file = open('./tweets/'+vaccine+'_10_most_positive_tweets.json')
        json_array = json.load(input_positive_file)
        tweet_array = []
        pubdate_array = []
        polarity_array = []
        # print(json_array)
        for key, value in json_array.items():
            if key == 'description':
                tweet_array = (list(value.values()))
            if key == 'pubdate':
                pubdate_array = list(value.values())
            if key == 'polarity':
                polarity_array = list(value.values())
        for iterator in range(len(tweet_array)):
            store_details = {"tweet": None, "date": None, "polarity": None}
            store_details["tweet"] = tweet_array[iterator]
            store_details["date"] = pubdate_array[iterator]
            store_details["polarity"] = polarity_array[iterator]
            store_positive_list.append(store_details)
    # print(store_positive_list)
    response_json = {"positive":store_positive_list,"negative":store_negative_list}
    json_object = json.dumps(response_json, indent=4)
    return json_object



@app.route("/wordcloud/<string:vaccine>", methods=['GET'])
def get_negative_neutral_positive_wordcloud(vaccine):
    value = ''
    base64_encoded_value = ''
    match vaccine.lower():
        case 'covaxin' | 'moderna' | 'sputnik' | 'sinopharm' | 'sinovac':
            value = vaccine.lower()
        case 'pfizer' | 'biontech':
            value = 'pfizer'
        case 'oxford' | 'astrazeneca':
            value = 'astrazeneca'
        case _:
            value = ''
    file = Path("./images/" + value + "_sentiment_wordclouds_revised_dataset.png")
    if file.is_file():
        with open(file, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        response = make_response(jsonify(
            {"value": encoded_string.decode('utf-8')}), 200, )
        app.logger.info("Wordcloud image successfully encoded for vaccine " + vaccine.lower() + "!")
    else:
        response = make_response(jsonify(
            {"value": ''}), 404, )  # for null-case when there is no wordcloud file for that vaccine
        app.logger.info(
            "Wordcloud image not available for vaccine " + vaccine.lower() + "! Need to compute for image generation...")
    response.headers["Content-Type"] = "application/json"
    return response


if __name__ == '__main__':
    app.run(debug=False, port=5050)
