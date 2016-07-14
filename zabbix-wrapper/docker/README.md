[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

INDIGO monitoring-wrapper
============================

This is the monitoring wrapper of the PaaS layer, a component of the INDIGO project. It exposes REST API wrapping the zabbix ones.
To make it properly work it should point to a zabbix server

You can find the REST APIs at [INDIGO OpenProject] (https://project.indigo-datacloud.eu/projects/wp5/wiki/Orchestrator_REST_APIs).


1. INSTALLATION
===============

1.1 REQUISITES
--------------

This project has been created with maven 3.3.3 and Java 1.8. Maven will take care of downloading the extra dependencies needed for the project but this project dependes on [im-java-api](https://github.com/indigo-dc/im-java-api) also.
To run the Orchestrator you need docker and a MySQL Server on your machine. See next section to have details.

1.2 INSTALLING
--------------

When having the war at disposal startin from a clean VM with Ubuntu install the docke manager:
```
sudo apt-get update
```
```
sudo apt-get install wget
```
```
sudo wget -qO- https://get.docker.com/ | sh
```

Install the application server (Wildfly 9.x) right from directory into which there is the docker file for giving the proper instructions and deploy the webapp
```
docker build -t indigodatacloud/zabbixwrapper .
```
```
docker logs -f `sudo docker run --name zabbixwrapper -h zabbixwrapper -p 80:8080 -d indigodatacloud/zabbixwrapper`
```

The deploy will be successfull if the endpoints written in the property file are correct and the wrapper can reach the server itself

## In case wrapper is not a war --> Compile the code
To compile the project you need to be in the same folder as the `pom.xml` file and type:
```
mvn clean install -DskipTests
```
This command compiles the code and skip the tests. If you want to compile the code running the tests too you can use:
```
mvn clean install
```

At compilation completed, the `MonitoringPillar.war` file will be put inside the `target` folder.

### Build the Docker image

The generated war must then be placed in the docker folder.

You can build the docker image with the command
```
docker build -t indigodatacloud/zabbixwrapper /path/to/the/docker/folder
```



