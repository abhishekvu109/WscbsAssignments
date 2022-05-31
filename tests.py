#!/usr/bin/env python3
import unittest

import compute as compute
import visualise as visualise


class TestCleansingAndEncoding(unittest.TestCase):
    def test_cleansing_tweet(self):
        """
        Test that it can clean hyperlinks, and other values
        """
        tweet = 'https://www.google.com Google is the best web-search engine indeed, RT again!'
        result = compute.clean_tweet_text(tweet)
        print(result)
        self.assertEqual(result, ' google is the best web-search engine indeed, again!')

    def test_base64_value(self):
        """
        Test that it can generate b4 encoding of an object properly
        """
        data = b'test'
        result = visualise.print_base64_value(data)
        print(data)
        self.assertEqual(result, b'dGVzdA==')


if __name__ == '__main__':
    unittest.main()
