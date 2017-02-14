# Indigo Monitoring Framework

The Monitoring Framework is a set of tools which allow performing several monitoring operations in the platform resulting from the INDIGO-Datacloud project (https://www.indigo-datacloud.eu/). The Monitoring Framework is based on Zabbix, as the collector of the monitoring information coming from different sources, due to its maturity, its community support and its flexibility for different environments.

The Monitoring Framework is divided in several main parts:
* The Zabbix server (with the corresponding configuration and some support scripts);
* The Zabbix wrapper, created for enabling a REST API for Zabbix;
* Several probes, with different monitoring purposes (OCCI, Heapster, etc.).

This repository contains the supporting scripts for the Zabbix server (in order to perform automatic backups of the Zabbix database and configuration), the wrapper to be deployed with Zabbix (as a way to facilitate integration) and the probes released in the first version: a probe for monitoring OCCI interfaces of Infrastructure Providers and a probe for monitoring the Kubernetes cluster where the Indigo platform is deployed (by means of the Heapster tool).

1. Zabbix Wrapper
=================

1.1 Main Features
-----------------
The first release of Zabbix Wrapper provides a Restful version of zabbix (which natively comes with JSON-RPC 2.0 protocol) APIs and potentially allows to develop a wrapper another products, therefore behaving as an adapter.
For indigo project purposes is meant to be the middle layer between Monitoring Framework and zabbix so that it can: 

* Create a host on zabbix platform (corresponding to, from Indigo point of view, a specific Cloud Provider)
* Get information about hosts (in indigo corresponding to specific providers)
* Get information about hostgroups (in indigo groups of providers)
* Get information about metrics based on configuration setup on zabbix

All these information are just returned in form of a REST response API which wrap zabbix ones.

1.2 Pre-Requisites
------------------
In order to get information and successfully monitor a specific cloud provider it has to be both registered on zabbix (via "create host" wrapper API) and there must be a zabbix agent installed and properly configured (for communicate with zabbix server) on board of a machine otherwise an exception will be thrown.

1.3 Installation
----------------

When having the war and a clean VM with Ubuntu at disposal, install the docker manager:
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
docker build -t indigodatacloud/zabbix-wrapper .
```
```
docker logs -f `docker run -d --name=indigo-zabbix-wrapper -e ZABBIX_URL=http://<url-zabbix>/api_jsonrpc.php -e ZABBIX_USERNAME=<zabbix-username> -e ZABBIX_PASSWORD=<zabbix-password> -p 8080:808 indigodatacloud/zabbix-wrapper` 
```

The deploy will be successfull if the endpoints written in the property file are correct and the wrapper can reach the server itself

##### In case wrapper is not a war --> Compile the code

To compile the project you need to be in the same folder as the `pom.xml` file and type:
```
mvn clean install
```
This command compiles the code and skip the tests. If you want to compile the code running the tests too you can use:
```
mvn clean install -DskipTests
```

At compilation completed, the `zabbix-wrapper.war` file will be inserted in the `target` folder.

#### Build the Docker image

The generated war must then be placed in the docker folder.

You can build the docker image with the command
```
docker build -t indigodatacloud/zabbix-wrapper/path/to/the/docker/folder
```
```
docker logs -f `docker run -d --name=indigo-zabbix-wrapper -e ZABBIX_URL=http://<url-zabbix>/api_jsonrpc.php -e ZABBIX_USERNAME=<zabbix-username> -e ZABBIX_PASSWORD=<zabbix-password>  --name zabbix-wrapper -h zabbix-wrapper  -p 8080:808 indigodatacloud/zabbix-wrapper` 
```

1.4 Configuration
----------------- 
The only configuration needed for the project is concerned with the parameters to be passed when launching docker run command as the following:
 1. `ZABBIX_URL`: zabbix url in the format http://{domain-name}/api_jsonrpc.php
 2. `ZABBIX_USERNAME`: Zabbix username
 3. `ZABBIX_PASSWORD`: Zabbix password

2. Zabbix Probes
================

The first release of the Monitoring Framework provides three probes for monitoring concrete aspects of the Indigo Platform:
* [A OCCI probe, which checks whether the OCCI API exposed by an Infrastructure Provider works as expected;](doc/OCCI.md)
* [A Heapster probe, which retrieves information about the containers and pods running in a Kubernetes cluster;](doc/heapster.md)
* [A Mesos cluster probe which will check the status and working conditions of the general Mesos cluster as well as the health status of the Chronos and Marathon instances.](doc/mesos.md)

The documentation for installation, configuration and running of each probe is described in their own page. To develop new probes, please see the [probe development guide](doc/probe_development.md).
