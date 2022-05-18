# pull base image
# FROM python:3.10-slim-buster
FROM alpine:latest

MAINTAINER Group12@WS&CBS

# set work directory
WORKDIR /app

# install updates and Python dependencies
RUN apt-get update -y && apt-get install -y python-pip python-dev

# set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# install dependencies
RUN pip install --upgrade pip
COPY ./requirements.txt /app/requirements.txt
RUN pip install -r requirements.txt

# copy images, tweets and python files and required python source files
COPY ./images /app/images
COPY ./tweets /app/tweets
COPY ./app.py /app/app.py
COPY ./data_operations.py /app/data_operations.py

EXPOSE 5000

# run python application in container
ENTRYPOINT [ "python" ]

# append below parameter(s) to ENTRYPOINT PARAMETER
CMD ["app.py" ]


# ------------------------------------------
# Docker build instructions below:
# ------------------------------------------
# docker build -t flask-producer:latest .
# docker run -d -p 5000:5000 flask-producer
# ------------------------------------------