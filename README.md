# URL Shortener

## Install JAVA-11 or above

## The app is tested on MySQL database . In-case of different database please change the connection string in application.properties

## Install a database (MySQL Database recommended)
### Create an user
CREATE USER 'group12'@'localhost' IDENTIFIED WITH  BY 'group12';
### Grant necessary privileges
GRANT ALL PRIVILEGES ON *.* TO 'group12'@'localhost' WITH GRANT OPTION;

### Flush privileges
FLUSH PRIVILEGES;

## Start the discovery service (Move inside the excutable directory and then navigate to the discovery)
### If the mysql runs on a different port then please change the port number in application.properties
java -jar discovery-service-final.jar --spring.config.location=classpath:application.properties,application.properties

## Start the User service service
java -jar auth-service-final.jar --spring.config.location=classpath:application.properties,application.properties

## Start the Url Shortener service
java -jar UrlShortner-final.jar --spring.config.location=classpath:application.properties,application.properties

