
# B2BFG Product

Application exposing RESTful APIs for carrying out CRUD operations for Product model and RESTful APIs for searching Products by title or description.

# Used Technologies

* Gradle
* GIT
* Java 8
* Mysql 5.7+
* Spring Boot
* Spring Data Jpa
* Spring Data Rest
* Liquibase
* Mockito

# Development Environment:

* Checkout the project (master branch is used)
* Import it in your favorite IDE (personally prefer IntelliJ) as a gradle project.
* Make sure you have Mysql 5.7+

# Deployment

You will need to update deploy.properties file first with your ssh args (assuming your public key is placed on the server), which will be used in the update process of the server (must be a sudoer on the server)

    ssh.key=root@127.0.0.1

To update server:

    ./gradlew clean deployOnServer

# Docker

You can use Docker. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

To Build docker image for the first time (attached):

    docker-compose -f src/main/docker/mysql.yml up --build

To Start docker image without sudo:

    sudo chmod 777 /var/run/docker.sock

For example, to start a mysql database in a docker container, run (mysql service must be stopped first):

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running (this will build to a docker daemon):

    ./gradlew jibDockerBuild

Then run:

    docker run -p 8080:8080/tcp b2b-assign-app:0.0.1-SNAPSHOT

To build to container image registry:

    ./gradlew jib --image=b2b-assign-app:latest

Question regarding deployment on AWS

    ElasticBeanstalk with EC2 and RDS or Deploy the docker img to ECS ?