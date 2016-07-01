# Indigo Monitoring Framework

The Monitoring Framewor is a set of tools which allow performing several monitoring operations in the platform resulting from the INDIGO-Datacloud project (https://www.indigo-datacloud.eu/). The Monitoring Framework is based on Zabbix, as the collector of the monitoring information coming from different sources, due to its maturity, its community support and its flexibility for different environments.

The Monitoring Framework is divided in several main parts:
* The Zabbix server (with the corresponding configuration and some support scripts);
* The Zabbix wrapper, created for enabling a REST API for Zabbix;
* Several probes, with different monitoring purposes (OCCI, Heapster, etc.).

This repository contains the supporting scripts for the Zabbix server (in order to perform automatic backups of the Zabbix database and configuration), the wrapper to be deployed with Zabbix (as a way to facilitate integration) and the probes released in the first version: a probe for monitoring OCCI interfaces of Infrastructure Providers and a probe for monitoring the Kubernetes cluster where the Indigo platform is deployed (by means of the Heapster tool).

1. Zabbix Scripts
=================

1.1 Main Features
-----------------


1.2 Pre-Requisites
------------------

1.3 Installation
----------------

1.4 Configuration
----------------- 

2. Zabbix Wrapper
=================

2.1 Main Features
-----------------


2.2 Pre-Requisites
------------------

2.3 Installation
----------------

2.4 Configuration
----------------- 

3. Zabbix Probes
================

3.1 Main Features
-----------------
The first release of the Monitoring Framework provides two probes for monitoring concrete aspects of the Indigo Platform:
* A OCCI probe, which checks whether the OCCI API exposed by an Infrastructure Provider works as expected;
* A Heapster probe, which retrieves information about the containers and pods running in a Kubernetes cluster.

In the case of the OCCI probe, the list of available providers is retrieved and, for each OCCI API available, a VM is creted, inspected and deleted, as a way to confirm that the main operations to be done are working as expected in the provider under evaluation. The probe is able to monitor several providers concurrently and it sends all the gathered metrics to the Zabbix server collecting all the information.

The Heapster probe, on the other hand, access to the Heapster API in order to list the pods and the containers available per pod, retrieving several metrics at the pod and container level, since they are complementary. These metrics, later on, are sent to the Zabbix server.

3.2 Pre-Requisites
------------------
Each probe has different requirements, since they rely on existing infrastructure to be monitored. Not fulfilling these requisites will have a negative impact in the execution of the probes.

The OCCI Probe has the following requisites:
* It requires a CMDB available, so it will be possible to retrieve the list of available providers. Having no access to a CMDB means that the probe will not be able to retrieve a list of providers to monitor, therefore not doing anything;
* It requires a Zabbix agent already installed, since the probe needs to run scripts provided by the Zabbix agent in order to send the metrics to the Zabbix server;
* It requires providers exposing OCCI APIs, otherwise, it will not be possible to monitor anything.

In the case of the Heapster probe, the requirements are the following:
* It is necessary to have a Heapster deployed in the corresponding Kubernetes cluster to be monitored, since the metrics are obtained from its APIs;
* It requires a Zabbix agent already installed, since the probe needs to run scripts provided by the Zabbix agent in order to send the metrics to the Zabbix server.

Since the implementation of the probes is in Java, both of them require, at least, a Java7 JVM to be already installed.

3.3 Installation
----------------


3.4 Configuration
----------------- 
The probes require different parameters to be configured in order to enable their operation.

The OCCI probe requires to modify the occiprobe.properties file in order to set the following parameters:
*openstack.user - Set here the user to be used for accessing OCCI APIs
*openstack.password - Set here the password to be used for accessing the OCCI APIs
*java.keystore - Set here the full location of the security certificates keystore
*zabbix.ip - Provide the IP address of the Zabbix server where metrics will be sent
*zabbix.sender.location - Configure the location where the Zabbix agent was installed, indicating the zabbix sender path
*cmdb.location - Provide the full URL of the CMDB component, providing the information about the available providers

The Heapster probe, on the other hand, requires the heapsterprobe.properties file to be adapted:
*heapster.url - Provide the URL where the Heapster API is available
*java.keystore - Set here the full location of the security certificates keystore
*zabbix.ip - Provide the IP address of the Zabbix server where metrics will be sent
*zabbix.sender.location - Configure the location where the Zabbix agent was installed, indicating the zabbix sender path

3.5 Potential Issues
--------------------
In the case of providers requiring some communication using SSL, if the provider certificate is not signed by a known entity, the JVM may throw exceptions. In such case, it is necessary to register the corresponding certificate with the following command:

keytool -importcert -trustcacerts -alias infnkeystone -file infnkeystone.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts"