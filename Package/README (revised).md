------------------------------------------------------------------------------------------------------------
# To run the docker images, for first part of the assignment     						|
# Please switch to the Package directory in the branch of 'assignment-3' of the GitHub project.    		|
------------------------------------------------------------------------------------------------------------

### !. Create the network and put all the services into a strongly connected topology (To reduce latency)
- sudo docker network create -d bridge group-12-network
- Retrieve the gateway IP of this network using "docker network inspect group-12-network", as that IP-address will be used to forward the requests to other microservices.



### 0. We will retrieve an automatic image of mysql from docker repository.
### MySQL service can be run on docker as a service
- docker pull mysql:latest
- docker run -p 3306:3306 --name mysqldatabase --net group-12-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=urlshortener -e MYSQL_USER=group12 -e MYSQL_PASSWORD=group12 -d mysql:latest
- If you need to start the existing instance(docker start mysqldatabase), and to stop the existing instance(docker stop mysqldatabase)
- If you need to stop or remove the running a docker container, use [sudo] docker stop <container-ID>, followed by [sudo] docker rm <container-ID> (HINT: get <container-ID> from docker ps)



### 1. Next is to build and start the discovery service (A service that acts as a server to other services)

#### 1.1. The first step is to build the image from inside the Package/DiscoveryService directory
cd DiscoveryService
docker build -t discovery-service-image .
#### 1.2. The second step is to run the image
sudo docker run --name discovery-service-app1 -d -p 5050:5050 --env-file env --network group-12-network discovery-service-image
cd ..


### 2. Next is to build and start the Auth service (A service that lets the users to register and login)
#### 2.1. The first step is to build the image from inside the Package/AuthService directory
cd AuthService
docker build -t auth-service-image .
#### 2.2. The second step is to run the image
sudo docker run --name auth-service-app1 -d -p 7070:7070 --env-file env --network group-12-network auth-service-image
cd ..


### 3. Next is to build and start the urlshortener service (A service that let the users to register and login)
#### 3.1. The first step is to build the image from inside the Package/UrlShortenerService directory
cd UrlShortenerService
docker build -t urlshortener-service-image .
#### 3.2. The second step is to run the image
docker run --name urlshortener-service-app1 -d -p 8080:8080 --env-file env --network group-12-network urlshortener-service-image
cd ..


### 4. Next is to build and start the nginx reverse proxy service (A service that acts as an entrypoint for other services)

#### 4.1. The first step is to build the image from inside the Package/NginxReverseProxy directory
cd NginxReverseProxy
docker build -t nginx-reverse-proxy-image .
#### 4.2. The second step is to run the image
sudo docker run --name nginx-reverse-proxy1 -d -p 6060:6060 --network group-12-network nginx-reverse-proxy-image
cd ..
-> For the testing of nginx reverse proxy, please make requests as listed below (after making the below hostnames entry in hosts file):
            http://url-shortener.service:6060/test (for quering url-shortener service with short-ID=test)
            http://user-auth.service:6060/test (for quering user-auth service with user-ID=test, and so on...)
