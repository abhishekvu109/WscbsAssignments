#!/usr/bin/env python3
import unittest

import compute as compute
import visualise as visualise


class TestCleansingAndEncoding(unittest.TestCase):
    def test_cleansing_tweet(self):
        """
        Test that it can clean hyperlinks, and other values
        """
        tweet = 'https://www.google.com Google is the best web-search engine indeed, RT again @World!'
        result = compute.clean_tweet_text(tweet)
        print(result)
        self.assertEqual(result, ' google is the best web-search engine indeed, again !')

    def test_base64_value(self):
        """
        Test that it can generate b4 encoding of an object properly
        """
        data = b'test'
        result = visualise.print_base64_value(data)
        print(result)
        self.assertEqual(result, b'dGVzdA==')


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


if __name__ == '__main__':
    unittest.main()
