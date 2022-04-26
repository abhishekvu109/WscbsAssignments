
# To run the docker images

### Create the network and put all the services into a strongly connected topology (To reduce latency)
- sudo docker network create mynetwork

### The first thing we have to do is to start the mysql service.
### MySQL service can be run on docker as a service
- docker pull mysql:latest
- docker run -p 3306:3306 --name mysqldatabase --net mynetwork -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=urlshortener -e MYSQL_USER=group12 -e MYSQL_PASSWORD=group12 -d mysql:latest
- If you need to start the existing instance(docker start mysqldatabase)
- If you need to stop the existing instance(docker stop mysqldatabase)



### 1. Next is to build and start the discovery service (A service that acts a server to the other services)

#### 1.1. The first step is to build the image
docker build -t discovery-service-image .
#### 1.2. The second step is to run the image
sudo docker run --name discovery-service-app1 -d -p 5050:5050 -e SERVICE_PORT=5050 --network mynetwork discovery-service-image
OR
sudo docker run --name discovery-service-app1 -d -p 5050:5050 --env-file env --network mynetwork discovery-service-image

### 2. Next is to build and start the Auth service (A service that let the users to register and login)
#### 2.1. The first step is to build the image
docker build -t auth-service-image .
#### 2.2. The second step is to run the image
sudo docker run --name auth-service-app1 -d -p 7070:7070 --env-file env --network mynetwork auth-service-image


### 3. Next is to build and start the urlshortener service
#### 3.1. The first step is to build the image
docker build -t urlshortener-service-image .
#### 3.2. The second step is to run the image
docker run --name urlshortener-service-app1 -d -p 8080:8080 --env-file env --network mynetwork urlshortener-service-image
