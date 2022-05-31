#!/usr/bin/env python3
import base64
from PIL import Image
from io import BytesIO


def vaccine_polarity_visualisation(vaccine):
    with open("./"+vaccine+"_timeseries_polarity_optimised_dataset.png", "rb") as image_file:
        file_bytes = image_file.read()
    encoded_string = print_base64_value(file_bytes)
    print(encoded_string.decode('utf-8'))
    image = Image.open(BytesIO(base64.b64decode(encoded_string)))
    # image.show()


def print_base64_value(file_bytes):
    encoded_string = base64.b64encode(file_bytes)
    return encoded_string


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    vaccine = input("Please enter vaccine name for which you want the sentiment analysis visualisation: \n")
    vaccine_polarity_visualisation(vaccine)