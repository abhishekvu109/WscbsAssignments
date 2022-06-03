import json
import flask
from flask import Flask, jsonify, request, make_response
from flask_cors import CORS, cross_origin
import base64
from pathlib import Path
import logging
import shutil

## @package app
#  This is a Python Flask API application which would retrieve the top-tweets and images computed by brane packages,
#  and return it to a front-end application (as a REST response).
#  It is a REST API service.


# initialising Flask app and configuring CORS (for handling Cross Origin Resource Sharing) for the application
app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'
# configuring the logger in DEBUG mode for detailed logging
logging.basicConfig(filename='flask_console.log', level=logging.DEBUG,
                    format='%(asctime)s %(levelname)s %(name)s: %(message)s')

tweet_dir = 'data/tweets'
images_dir = 'data/images'


# a generic function to return response based on the return data and status value
def return_response(return_data, status):
    ##
    # @brief A generic function to return response based on the return data and status value
    #
    # @param return_data data/information to be returned
    # @param status HTTP status code
    #
    response = flask.make_response(jsonify(return_data))
    response.headers['Access-Control-Allow-Headers'] = '*'
    response.status_code = status
    return response


@app.route('/')
@cross_origin()
def hello_world():
    ##
    # @brief a small test function for checking that API is accessible
    #
    app.logger.info("Home URL accessed!")
    return 'Hello World!'


@app.route("/timeseries/<string:vaccine>", methods=['GET'])
@cross_origin()
def time_series_analysis(vaccine):
    ##
    # @brief Function to return the base64 encoding of the time-series analysis plot for passed vaccine name
    #
    # @param vaccine input vaccine name
    #
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
        return return_response({"value": encoded_string.decode('utf-8')}, 203)
        # return return_response({"value": ""}, 404)


@app.route("/tweets/<string:vaccine>", methods=['GET'])
@cross_origin()
def get_negative_positive_tweets(vaccine):
    ##
    # @brief Return the top 10 positive and negative tweets from the data-set for the input vaccine name
    #
    # @param vaccine input vaccine name
    #
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
        return return_response({"value": ""}, 203)


def custom_json_parsing(vaccine):
    ##
    # @brief Function to parse the json data (written by the brane-compute package) in a better organised format
    # @param vaccine input vaccine name
    #
    store_negative_list = []
    store_positive_list = []
    # formatting the json for negative tweets
    file = Path(tweet_dir + "/" +vaccine+'_10_most_negative_tweets.json')
    if file.is_file():
        input_negative_file = open(tweet_dir + "/" + vaccine +'_10_most_negative_tweets.json')
        json_array = json.load(input_negative_file)
        tweet_array = []
        pubdate_array = []
        polarity_array = []

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

    # formatting the json for positive tweets
    file = Path(tweet_dir + "/" + vaccine + '_10_most_positive_tweets.json')
    if file.is_file():
        input_positive_file = open(tweet_dir + "/" +vaccine+'_10_most_positive_tweets.json')
        json_array = json.load(input_positive_file)
        tweet_array = []
        pubdate_array = []
        polarity_array = []
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
    ##
    # @brief Function to return the base64 encoding of the wordcloud (consisting of positive, negative and neutral words)
    # for passed vaccine name.
    # @param vaccine input vaccine name
    #
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
        return return_response({"value": encoded_string.decode('utf-8')}, 203)
    # response.headers["Content-Type"] = "application/json"
    # return response


# Run the Python Flask API application
if __name__ == '__main__':
    Path('./' + images_dir).mkdir(parents=True, exist_ok=True)
    Path('./' + tweet_dir).mkdir(parents=True, exist_ok=True)
    app.run(debug=False, host='0.0.0.0', port=5050)
