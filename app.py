import json
import flask
from flask import Flask, jsonify, request, make_response
from flask_cors import CORS, cross_origin
import base64
from pathlib import Path
import logging
import shutil


app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'
logging.basicConfig(filename='flask_console.log', level=logging.DEBUG,
                    format='%(asctime)s %(levelname)s %(name)s: %(message)s')

tweet_dir = 'data/tweets'
images_dir = 'data/images'


def return_response(return_data, status):
    response = flask.make_response(jsonify(return_data))
    response.headers['Access-Control-Allow-Headers'] = '*'
    # response.headers['Access-Control-Allow-Origin'] = '*'
    response.status_code = status
    return response


@app.route('/')
@cross_origin()
def hello_world():  # put application's code here
    app.logger.info("Home URL accessed!")
    return 'Hello World!'


@app.route("/timeseries/<string:vaccine>", methods=['GET'])
@cross_origin()
def time_series_analysis(vaccine):
    value = ''
    base64_encoded_value = ''
    if vaccine.lower() == 'covaxin' or vaccine.lower() == 'moderna' or vaccine.lower() == 'sputnik' or vaccine.lower() == 'sinopharm' or vaccine.lower() == 'sinovac':
        value = vaccine.lower()

    elif vaccine.lower() == 'pfizer' or vaccine.lower() == 'biontech':
        value = 'pfizer'
    elif vaccine.lower() == 'oxford' or vaccine.lower() == 'astrazeneca':
        value = 'astrazeneca'
    else:
        value = ''
    file = Path(images_dir + "/" + value + "_timeseries_polarity_optimised_dataset.png")
    if file.is_file():
        with open(file, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        # response = make_response(jsonify(
        #     {"value": encoded_string.decode('utf-8')}), 200, )
        app.logger.info("Timeseries analysis image successfully encoded for vaccine " + vaccine.lower() + "!")
        return return_response({"value": encoded_string.decode('utf-8')}, 203)
    else:
        # response = make_response(jsonify(
        #     {"value": ''}), 404, )  # for null-case when there is no wordcloud file for that vaccine
        src = './data/image_na.png'
        dst = images_dir + '/' + vaccine + "_timeseries_polarity_optimised_dataset.png"
        shutil.copy(src, dst)
        app.logger.info(
            "Timeseries analysis image not available for vaccine " + vaccine.lower() + "! Showing the \'not "
                                                                                       "available\' image...")
        with open(dst, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        return return_response({"value": encoded_string.decode('utf-8')}, 404)
        # return return_response({"value": ""}, 404)


@app.route("/tweets/<string:vaccine>", methods=['GET'])
@cross_origin()
def get_negative_neutral_positive_tweets(vaccine):
    value = ''
    if vaccine.lower() == 'covaxin' or vaccine.lower() == 'moderna' or vaccine.lower() == 'sputnik' or vaccine.lower() == 'sinopharm' or vaccine.lower() == 'sinovac':
        value = vaccine.lower()

    elif vaccine.lower() == 'pfizer' or vaccine.lower() == 'biontech':
        value = 'pfizer'
    elif vaccine.lower() == 'oxford' or vaccine.lower() == 'astrazeneca':
        value = 'astrazeneca'
    else:
        value = ''
    if value:
        # response = make_response(custom_json_parsing(vaccine), 200, )
        app.logger.info("Positive and negative tweets available for vaccine  " + vaccine.lower() + "!")
        return return_response(custom_json_parsing(value), 200)
    else:
        # response = make_response(jsonify(
        #     {"value": ""}), 404, )  # for null-case when there is no wordcloud file for that vaccine
        app.logger.info(
            "Positive and negative tweets not available for vaccine " + vaccine.lower() + "! Need to compute tweet "
                                                                                          "JSON files...")
        return return_response({"value": ""}, 404)


def custom_json_parsing(vaccine):
    store_negative_list = []
    store_positive_list = []
    # for negative tweets
    file = Path(tweet_dir + "/" +vaccine+'_10_most_negative_tweets.json')
    if file.is_file():
        input_negative_file = open(tweet_dir + "/" + vaccine +'_10_most_negative_tweets.json')
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
    file = Path(tweet_dir + "/" + vaccine + '_10_most_positive_tweets.json')
    if file.is_file():
        input_positive_file = open(tweet_dir + "/" +vaccine+'_10_most_positive_tweets.json')
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
@cross_origin()
def get_negative_neutral_positive_wordcloud(vaccine):
    value = ''
    base64_encoded_value = ''
    if vaccine.lower() == 'covaxin' or vaccine.lower() == 'moderna' or vaccine.lower() == 'sputnik' or vaccine.lower() == 'sinopharm'  or vaccine.lower() == 'sinovac':
        value = vaccine.lower()

    elif vaccine.lower() == 'pfizer' or vaccine.lower() == 'biontech':
        value = 'pfizer'
    elif vaccine.lower() == 'oxford' or vaccine.lower() == 'astrazeneca':
        value = 'astrazeneca'
    else:
        value = ''
    file = Path(images_dir + "/" + value + "_sentiment_wordclouds_optimised_dataset.png")
    if file.is_file():
        with open(file, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        # response = make_response(jsonify(
        #     {"value": encoded_string.decode('utf-8')}), 200, )
        app.logger.info("Wordcloud image successfully encoded for vaccine " + vaccine.lower() + "!")
        return return_response({"value": encoded_string.decode('utf-8')}, 200)
    else:
        src = './data/image_na.png'
        dst = images_dir + '/' + vaccine + "_sentiment_wordclouds_optimised_dataset.png"
        shutil.copy(src, dst)
        app.logger.info(
            "Wordcloud image not available for vaccine " + vaccine.lower() + "! Showing the \'not "
                                                                                       "available\' image...")
        with open(dst, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        return return_response({"value": encoded_string.decode('utf-8')}, 404)
    # response.headers["Content-Type"] = "application/json"
    # return response


if __name__ == '__main__':
    Path('./' + images_dir).mkdir(parents=True, exist_ok=True)
    Path('./' + tweet_dir).mkdir(parents=True, exist_ok=True)
    app.run(debug=False, host='0.0.0.0', port=5050)
