from flask import request, Response, abort
from flask_restful import Resource
import requests


URL_USERS_WEB_SERVICE = 'http://127.0.0.1:7070/api/auth/user/'
URL_SHORTENER_WEB_SERVICE = 'http://127.0.0.1:8080/api/urlshortener/'


class Users(Resource):
    def post(self):
        # if not request.is_json:
        #     abort(400, msg='MRequest not in JSON!')
        url = URL_USERS_WEB_SERVICE
        return _proxy(request, url)


class UsersLogin(Resource):
    def post(self):
        url = URL_USERS_WEB_SERVICE
        return _proxy(request, url)


class Shortened_URL_ID(Resource):
    def get(self, id):
        url = URL_SHORTENER_WEB_SERVICE
        return _proxy(request, url)
        # return {"hello":f"{id}"}

    def put(self, id):
        url = URL_SHORTENER_WEB_SERVICE
        return _proxy(request, url)

    def delete(self, id):
        url = URL_SHORTENER_WEB_SERVICE
        return _proxy(request, url)


class Shortened_URL(Resource):
    def get(self):
        url = URL_SHORTENER_WEB_SERVICE
        return _proxy(request, url)


    def post(self):
        url = URL_SHORTENER_WEB_SERVICE
        return _proxy(request, url)


    def delete(self):
        url = URL_SHORTENER_WEB_SERVICE
        return _proxy(request, url)


# referenced from https://stackoverflow.com/a/36601467/3482140
def _proxy(request, required_url):
    resp = requests.request(
        method=request.method,
        url=request.url.replace(request.host_url, required_url),
        headers={key: value for (key, value) in request.headers if key != 'Host'},
        data=request.get_data(),
        cookies=request.cookies,
        allow_redirects=False)

    excluded_headers = ['content-encoding', 'content-length', 'transfer-encoding', 'connection']
    headers = [(name, value) for (name, value) in resp.raw.headers.items()
               if name.lower() not in excluded_headers]

    response = Response(response=resp.content, status=resp.status_code,
                        headers=headers)
    return response
