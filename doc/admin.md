# Deployment and Administration Guide

This guide aims at providing information about how to install, configure and administrate the main components included in the Monitoring repository. While in the case of the Zabbix Wrapper it is possible to deploy directly a Docker container, the Zabbix probes are currently released in the form of deb and rpm packages, although a Docker container is being prepared as well, in order to facilitate their deployment in those infrastructures based on Docker (such as the Indigo PaaS platform, hosted in a Kubernetes cluster).

Both, the Zabbix Wrapper and the probes, have been implemented in Java and they use Maven in order to build the jar files and the packages. Therefore, it is possible to download the source code and use Maven commands for compiling the code, or to use the already generated packages. This guide focuses on the generated packages and on the Docker image for the Wrapper.

1. Zabbix Wrapper
=================
1.1 Installation
----------------

When having the war at disposal starting from a clean VM with Ubuntu install the docker manager:
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

#### In case wrapper is not a war --> Compile the code

To compile the project you need to be in the same folder as the `pom.xml` file and type:
```
mvn clean install -DskipTests
```
This command compiles the code and skip the tests. If you want to compile the code running the tests too you can use:
```
mvn clean install
```

At compilation completed, the `MonitoringPillar.war` file will be put inside the `target` folder.

#### Build the Docker image

The generated war must then be placed in the docker folder.

You can build the docker image with the command
```
docker build -t indigodatacloud/zabbixwrapper /path/to/the/docker/folder
```

1.2 Configuration
----------------- 
This project has been created with maven 3.3.3 and Java 1.8. Maven will take care of downloading the extra dependencies needed for the project but this project dependes on im-java-api also. To run the warpper you need docker and a MySQL Server on your machine. See next section to have details.

2. Zabbix Probes
================

2.1 Installation
----------------

First of all, make sure the main dependencies of the probes are available: the JRE and the Zabbix Agent. If this is not the case, they can be installed following a few simple steps.

In order to install the JRE:
* Ubuntu:
```
sudo apt-get install default-jre
```

* CentOS:
```
sudo yum install java-1.7.0-openjdk
```

In order to install the Zabbix Agent, it is necessary to run the following commands (see https://www.zabbix.com/documentation/3.0/manual/installation/install_from_packages):

* Ubuntu (look at the link for repositories configuration):
```
apt-get install zabbix-agent
```

* CentOS:
```
yum install zabbix-agent
```

Then, it is necessary to install the corresponding packages generated for the probes.

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/occi-zabbix-probe-1.01.deb
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/heapster-zabbix-probe/heapster-zabbix-probe-1.01.deb

dpkg --install occi-zabbix-probe-1.01.deb
dpkg --install heapster-zabbix-probe-1.01.deb
```

* CentOS
```
sudo yum install https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/OCCIZabbixProbe-1.01.rpm
sudo yum install https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/heapster-zabbix-probe/HeapsterZabbixProbe-1.01.rpm
```

Installing the packages will deploy the corresponding jar files in the '/usr/share/java/zabbix/' folder, where it will be possible to run them with the corresponding 'java' command.

Although the probes can be run just on demand, the best option is to configure them as Cron jobs. That can be configured by editing the configuration file with the following command:
```
crontab -e
```

Then, add the following lines:
```
0 * * * * java -jar /usr/share/java/zabbix/occi-zabbix-probe-0.95-jar-with-dependencies.jar
30 * * * * java -jar /usr/share/java/zabbix/heapster-zabbix-probe-0.95-jar-with-dependencies.jar
```

This means that the probes will run every hour, one at xx:00 and the other one at xx:30 (avoiding potential issues when both probes aim at using the Zabbix Agent at the same time). Modify this configuration according to your needs, so the probes will run when required, but bear in mind that running them in parallel may create issues with the Zabbix Agent, since it is not ready to work with concurrent tasks.

2.2 Configuration
----------------- 
The probes require different parameters to be configured in order to enable their operation.

The OCCI probe requires to modify the occiprobe.properties file in order to set the following parameters:
* openstack.user - Set here the user to be used for accessing OCCI APIs
* openstack.password - Set here the password to be used for accessing the OCCI APIs
* java.keystore - Set here the full location of the security certificates keystore
* zabbix.ip - Provide the IP address of the Zabbix server where metrics will be sent
* zabbix.sender.location - Configure the location where the Zabbix agent was installed, indicating the zabbix sender path
* cmdb.location - Provide the full URL of the CMDB component, providing the information about the available providers

The Heapster probe, on the other hand, requires the heapsterprobe.properties file to be adapted:
* heapster.url - Provide the URL where the Heapster API is available
* java.keystore - Set here the full location of the security certificates keystore
* zabbix.ip - Provide the IP address of the Zabbix server where metrics will be sent
* zabbix.sender.location - Configure the location where the Zabbix agent was installed, indicating the zabbix sender path

2.3 Packages Update
-------------------
If a previous version of the packages is already installed, it is necessary to update them to the new version. That can be done through the following commands.

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/occi-zabbix-probe-1.01.deb
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/heapster-zabbix-probe/heapster-zabbix-probe-1.01.deb

dpkg -i occi-zabbix-probe-1.01.deb
dpkg -i heapster-zabbix-probe-1.01.deb
```

* CentOS
```
sudo rpm -Uvh https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/OCCIZabbixProbe-1.01.rpm
sudo rpm -Uvh https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/heapster-zabbix-probe/HeapsterZabbixProbe-1.01.rpm
```

2.4 Accepted Certificates
-------------------------
In the case of providers requiring some communication using SSL (because of HTTPS communication), if the provider certificate is not signed by a known entity, the JVM may throw exceptions. In such case, it is necessary to register the corresponding certificate with the following command:
```
keytool -importcert -trustcacerts -alias infnkeystone -file infnkeystone.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts"
```
