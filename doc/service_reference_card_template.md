# Monitoring Service Reference Card

Daemons running
===============
There are no daemons running for the probes and the wrapper.

Init scripts and options
========================
The probes can be run directly invoking the corresponding commands, although it is recommended to configure them as cron jobs, so they will be executed periodically, without the need of any other intervention.

Zabbix Wrapper
--------------
Zabbix wrapper can be run by launching the command, as long as docker process is installed on board of the machine:
```
docker logs -f `sudo docker run --name zabbix-wrapper -e ZABBIX_URL=http://<url-zabbix>/api_jsonrpc.php -e ZABBIX_USERNAME=<zabbix-username> -e ZABBIX_PASSWORD=<zabbix-password> -p 80:8080 -d indigodatacloud/zabbix-wrapper`
```
 
OCCI Probe
----------
The OCCI probe can be executed as follows:
```
java -jar /usr/share/java/zabbix/occi-zabbix-probe-1.01-jar-with-dependencies.jar  
```

Heapster Probe
--------------
The Heapster probe can be executed as follows:
```  
java -jar /usr/share/java/zabbix/heapster-zabbix-probe-1.01-jar-with-dependencies.jar
```

Mesos Probe
--------------
The Heapster probe can be executed as follows:
```  
mesosprobe.sh <command>
```
where command is `mesos`, `chronos` or `marathon` depending on the system that needs to be monitored

  
Configuration files location with example or template
=====================================================
Zabbix Wrapper
--------------
The configuration file is populated with the parameters just shown on above command at container launch time. It can be found by entering into the docker container at: /opt/jboss/wildfly-9.0.2.Final/standalone/deployment/wrapper-zabbix/WEB-INF/classes/zone.yml
  
OCCI Probe
----------
The configuration file for the OCCI probe is deployed by default at: /etc/zabbix/occiprobe.properties
  
Heapster Probe
--------------
The configuration file for the Heapster probe is deployed by default at: /etc/zabbix/heapsterprobe.properties

Mesos Probe
--------------
The configuration file for the Heapster probe is deployed by default at: /etc/zabbix/mesosprobe.properties
  
Logfile locations (and management) and other useful audit information
=====================================================================
Zabbix Wrapper
--------------
The current version of the wrapper only shows some messages in the console when it is running. They can be seen by running the command:
```
docker logs -f --tail=1000 zabbix-wrapper
```
  
OCCI Probe
----------
The current version of the probe only shows some messages in the console when it is running. There is an ongoing update for generating log files. For now, we suggest to redirect the output of the probe to a file. This can be done with a command such as:
```
java -jar /usr/share/java/zabbix/occi-zabbix-probe-1.01-jar-with-dependencies.jar > occi.log
```
  
Heapster Probe
--------------
The current version of the probe only shows some messages in the console when it is running. There is an ongoing update for generating log files. For now, we suggest to redirect the output of the probe to a file. This can be done with a command such as:
```
java -jar /usr/share/java/zabbix/heapster-zabbix-probe-1.01-jar-with-dependencies.jar > heapster.log 
```

Mesos Probe
--------------
There is a default configuration file at /etc/zabbix/mesosprobe-log.properties that will instruct it to log both to the standand output and a rolling file at /var/log/mesosprobe<number>.log files. 
This configuration file might be modified in order to debug and/or change the log file destination.
    
Open ports
==========
Zabbix Wrapper
--------------
By use of docker the wrapper can be basically exposed on any port, as long as there is not any other process listening from that port, just like show in the command for launching the docker container: 
```
-p 80:8080
```
8080 is the port where wildfly listens from. 80 is the exposed port.
  
OCCI Probe
----------
The probe should be installed close to the Zabbix server and, therefore, there should not be any issue with the default port used by the Zabbix agent for communicating (10051). In case the probe and the Zabbix server are not deployed in the same network, bear in mind that the mentioned port needs to be opened.
  
On the other hand, the probe requires to access remote Cloud providers through their OCCI API, which means that it will be necessary that those ports where the OCCI APIs are exposed must be opened. This information comes from the CMDB component and it is not configurable in the probe.
  
Heapster Probe
--------------
The probe should be installed close to the Zabbix server and, therefore, there should not be any issue with the default port used by the Zabbix agent for communicating (10051). In case the probe and the Zabbix server are not deployed in the same network, bear in mind that the mentioned port needs to be opened.

OCCI Probe
----------
The probe should be installed close to the Zabbix server and, therefore, there should not be any issue with the default port used by the Zabbix agent for communicating (10051). In case the probe and the Zabbix server are not deployed in the same network, bear in mind that the mentioned port needs to be opened.

For Mesos, Chronos and Marathon, the API ports should be open so the probe can access the metrics and functionality needed to do its job.

Possible unit test of the service
=================================
Unit testing of the services are available for the probes and for the Zabbix wrapper and they can be run using Maven commands. Moreover, every time the code is built, Maven runs automatically all the tests defined, indicating any problem found.

Zabbix Wrapper
--------------
Since it has been developed as a Maven project, it is possible to run the unit tests with the corresponding Maven command, from the folder where the pom file and the source code is available. It has been used cobertura as plugin to show the percentage of covered code. 
```
mvn clean cobertura:cobertura -Dcobertura.report.format=html
```
But even a more basic command to test whether the tests are successful is:
```
mvn test
```
  
OCCI Probe
----------
Since it has been developed as a Maven project, it is possible to run the unit tests with the corresponding Maven command, from the folder where the pom file and the source code is available:
```
mvn test
```

Heapster Probe
--------------
Since it has been developed as a Maven project, it is possible to run the unit tests with the corresponding Maven command, from the folder where the pom file and the source code is available:
```
mvn test
```

Mesos Probe
--------------
All three probes share most of the code which has been extracted to zabbix-probes-common library, which might be useful in the future development of probes. In order to run this tests, do it from this project by running:
```
mvn test
```

Where is service state held
===========================
The wrapper and the probes do not have to keep any state. These are stateless services.

Cron jobs
=========
Zabbix Wrapper
--------------
The Zabbix Wrapper has no associated cron jobs.

OCCI Probe
----------
We recommend to configure a cron job for executing the probe periodically, as follows:
```
0 * * * * java -jar /usr/share/java/zabbix/occi-zabbix-probe-1.01-jar-with-dependencies.jar  
```

Heapster Probe
--------------
We recommend to configure a cron job for executing the probe periodically, as follows:
```  
30 * * * * java -jar /usr/share/java/zabbix/heapster-zabbix-probe-1.01-jar-with-dependencies.jar
```

Mesos Probe
--------------
We recommend to configure a cron job for executing the probe periodically, as follows:
```
00 * * * * mesosprobe.sh mesos
15 * * * * mesosprobe.sh chronos
30 * * * * mesosprobe.sh marathon
```

Security information
====================
Access control Mechanism description (authentication & authorization)
---------------------------------------------------------------------
Neither the Zabbix probes or the Zabbix wrapper require authorization and authentication for accessing their functionality. But, the OCCI probe requires to configure some authentication information for getting access to the Cloud providers registered in INDIGO. The current version of the probe requires to configure a monitoring user and password, but there is an ongoing update for easing the authentication mechanism. Therefore, the OCCI probe will include a client for the IAM component, so the OCCI probe will act on behalf of the Monitoring system when performing the corresponding operations.
Mesos probe needs username and password for Chronos and Marathon functionality but no authentication is necessary for the Mesos cluster itself.
 
How to block/ban a user
-----------------------
Does not apply to this service.

Network Usage
-------------
The OCCI probe requires access to external Cloud providers for accessing their OCCI APIs. The rest of components do not need network access, although it is important that the Zabbix Wrapper is accessible externally, since other external components may need to use it for accessing monitoring metrics.

Firewall configuration
-----------------------
No especial configuration is needed.

Security recommendations
------------------------
For the OCCI probe, in the case of providers requiring some communication using SSL (because of HTTPS communication), if the provider certificate is not signed by a known entity, the JVM may throw exceptions. In such case, it is necessary to register the corresponding certificate with the following command:
```
keytool -importcert -trustcacerts -alias infnkeystone -file infnkeystone.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts"
```