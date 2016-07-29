# Developer Guide

1. Code Available
-----------------
The Monitoring Infrastructure for Indigo is divided in several parts:
* Zabbix Server
* Zabbix Wrapper
* Zabbix Probes

The Zabbix Server used is the version 3.0, which can be downloaded from its official website (although a container is already pre-configured in Indigo for this purpose).

This repository focuses on two main parts:
* The Zabbix Wrapper (/zabbix_wrapper)
* The Zabbix Probes for OCCI and Heapster (/zabbix-probes)

The components have been implemented in Java following the 'Google' code style, which has to be followed and is validated at building time.

2. Import Code
--------------
Since the Zabbix Wrapper and the Zabbix probes are implemented in Java, we decided to use Maven, in order to facilitate dependencies management, building the code and packaging the components adequately. This means that each component provides a pom file which can be used in any Java IDE (i.e. Eclipse), in order to import the project properties, as a way to reduce the complexity of getting started. In fact, thanks to the pom file, the project configuration is IDE agnostic.

There are three pom files:
* Zabbix Wrapper: Under the '/zabbix_wrapper' folder
* Zabbix OCCI probe: Under the '/zabbix-probes/occi-zabbix-probe' folder
* Zabbix Heapster probe: Under the '/zabbix-probes/heapster-zabbix-probe' folder

Just import the corresponding pom.xml file in your IDE and you will be able to start coding.

3. Build and Package
--------------------
The Maven projects have been configured in such a way that they validate the code style. In order to guarantee the code style is followed appropiately, it is only necessary to run Maven in the following way:
```
mvn validate
```

The corresponding Maven plugin will generate a 'checkstyle-result.xml' file in the target folder, indicating the errors found.

In order to package the code, the Maven project has been configured in such a way that it generate, first, a jar file with all the dependencies and, later on, it packages this jar in deb and rpm formats, in order to support Ubuntu and CentOS distributions. The corresponding maven command is:
```
mvn clean package
```

The jar file, as well as the packages, will be available in the configured target folder.