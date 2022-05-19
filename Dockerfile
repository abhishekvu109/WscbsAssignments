# pull base image
# FROM python:3.10-slim-buster
FROM ubuntu:latest

MAINTAINER Group12@WS&CBS

# set work directory
WORKDIR /app

# install updates and Python dependencies
RUN apt-get update -y && apt-get install -y python3-pip pip python3.10-dev
# RUN apk add python-pip python-dev --quiet

# set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# install dependencies
# RUN pip install --upgrade pip
COPY ./requirements.txt /app/requirements.txt
RUN pip install -r requirements.txt

# copy images, tweets and python files and required python source files
COPY ./images /app/images
COPY ./tweets /app/tweets
COPY ./app.py /app/app.py
COPY ./data_operations.py /app/data_operations.py

EXPOSE 5050

# run python application in container
ENTRYPOINT [ "python3" ]

# append below parameter(s) to ENTRYPOINT PARAMETER
CMD ["app.py" ]


# ------------------------------------------
# Docker build instructions below:
# ------------------------------------------
# docker build -t flask-producer:latest .
# docker run -d -p 5050:5050 --name flask-app flask-producer
# For troubleshooting the container, use `docker exec -it flask-app bash`
# ------------------------------------------
