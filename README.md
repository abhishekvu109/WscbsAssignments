# vaccine-tweets

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


<<<<<<< HEAD
## Docker

### Build
```
docker build -t webapp-consumer .
```

### Run

To run the application it should be in the Brane network. 

```
sudo docker run --name webapp-consumer1 -d -p 8080:8080 --mount src=/home/student101/project/data,target=/data/,type=bind --network brane webapp-consumer
```

## Setup

If you want to checkout the actual files where the frontend was developed, please look at the `/src` directory. Within this directory you will see:
- `/assets` : here you can see examples of top-tweets in .json and images in .png
- `/components` : where we store 3 main pages components:  wordcloud, timeseries and top-ten-tweets
- `/router` : the index file in it is responsible for the routing between pages
- `/store` : is the location where all the incoming data is stored
- `App.vue` : is the root .vue file of the application
- `main.js` : where the app is created and binded with other libraries
- `index.css` : used for design layout of the page


## Screenshots

Here you can see some screenshots of the website:

### Wordcloud
![wordcloud](/screenshots/wordcloud.png)

### Timeseries
![wordcloud](/screenshots/timeseries.png)

### Top ten tweets
![wordcloud](/screenshots/topten.png)
