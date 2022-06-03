#!/usr/bin/env python3
import unittest

import compute as compute
import visualise as visualise


## @package tests
#  This module is used for validating the unit tests for COVID-19 vaccine sentiment analysis project.
#  The tasks performed are validating the vaccine name, cleansing of tweet, and verifying the base64 calculation.
#  Assertions are validated, and the build is validated automatically using GitHub Actions.


## This class consists of 3 unit tests.
#  The tasks performed are validating the vaccine name, cleansing of tweet, and verifying the base64 calculation.
#  Respective functions are documented in their scope.
class TestCleansingAndEncoding(unittest.TestCase):


    ## Validates against the cleansing of tweet as per guidelines (hyperlinks, @, # and RT letters not allowed).
    #  @param self The object pointer.
    def test_cleansing_tweet(self):
        """
        Test that it can clean hyperlinks, and other unneeded values (@, # and RT)
        """
        tweet = 'https://www.google.com Google is the best web-search engine indeed, RT again @World!'
        result = compute.clean_tweet_text(tweet)
        print(result)
        self.assertEqual(result, ' google is the best web-search engine indeed, again !')


    ## Validates that the base64 value of the bytes is correct.
    #  @param self The object pointer.
    def test_base64_value(self):
        """
        Test that it can generate b4 encoding of an object properly
        """
        data = b'test'
        result = visualise.print_base64_value(data)
        print(result)
        self.assertEqual(result, b'dGVzdA==')


    ## Validates whether the input vaccine name is amongst the pre-configured 7 vaccines:
    # ['covaxin', 'sinopharm', 'sinovac', 'moderna', 'pfizer', 'biontech', 'oxford', 'astrazeneca', 'sputnik']
    #  @param self The object pointer.
    def test_valid_vaccine(self):
        """
        Test that only valid and supported vaccine names (7 in our case) are allowed
        ['covaxin', 'sinopharm', 'sinovac', 'moderna', 'pfizer', 'biontech', 'oxford', 'astrazeneca', 'sputnik']
        """
        data = 'moderna'
        result = visualise.validate_vaccine_name(data)
        print(result)
        self.assertTrue(result)
        data = 'modern'
        result = visualise.validate_vaccine_name(data)
        print(result)
        self.assertFalse(result)


# Run the unit tests
if __name__ == '__main__':
    unittest.main()
