from flask import Flask, jsonify
from flask_restful import Resource, Api
from functools import wraps
from services import Users, UsersLogin, Shortened_URL_ID, Shortened_URL

# API gateway help source: https://medium.com/zmninja/building-your-own-machine-learning-api-gateway-routes-db-and-security-part-ii-bf52f690c392
# python help: https://stackoverflow.com/questions/25098661/flask-restful-add-resource-parameters

app = Flask(__name__)
api = Api(app)


def get_http_exception_handler(app):
    """Overrides the default http exception handler to return JSON."""
    handle_http_exception = app.handle_http_exception
    @wraps(handle_http_exception)
    def ret_val(exception):
        exc = handle_http_exception(exception)
        return jsonify({'code': exc.code, 'msg': exc.description}), exc.code

    return ret_val


class HelloWorld(Resource):
    def get(self):
        return {'message':'This is a test call for verification of functionality of API gateway!'}


api.add_resource(HelloWorld, '/test')
api.add_resource(Users,'/users')
api.add_resource(UsersLogin,'/users/login')
api.add_resource(Shortened_URL_ID, '/<id>')
api.add_resource(Shortened_URL, '/')


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    app.run(debug=False, port=5050)

