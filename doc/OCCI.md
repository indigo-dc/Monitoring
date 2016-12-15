1 Main Features
-----------------
This probe works by getting the list of available providers and, for each OCCI API available, a VM is created, inspected and deleted, as a way to confirm that the main operations to be done are working as expected in the provider under evaluation. The probe is able to monitor several providers concurrently and it sends all the gathered metrics to the Zabbix server collecting all the information.

2 Pre-Requisites
------------------
Not fulfilling these requisites will have a negative impact in the execution of the probes.

* It requires a CMDB available, so it will be possible to retrieve the list of available providers. Having no access to a CMDB means that the probe will not be able to retrieve a list of providers to monitor, therefore not doing anything;
* It requires a Zabbix agent already installed, since the probe needs to run scripts provided by the Zabbix agent in order to send the metrics to the Zabbix server;
* It requires providers exposing OCCI APIs, otherwise, it will not be possible to monitor anything.

Since the implementation of the probe is in Java, it requires, at least, a Java7 JVM to be already installed.

3 Installation
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

Then, it is necessary to install the corresponding packages:

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/occi-zabbix-probe-1.01.deb
```
```
dpkg --install occi-zabbix-probe-1.01.deb
```

* CentOS
```
sudo yum install https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/OCCIZabbixProbe-1.01.rpm
```

Although the probes can be run just on demand, the best option is to configure them as Cron jobs. That can be configured by editing the configuration file with the following command:
```
crontab -e
```

Then, add the following lines:
```
0 * * * * java -jar /usr/share/java/zabbix/occi-zabbix-probe-1.01-jar-with-dependencies.jar
```

This means that the probe will run every hour at xx:00 please note that when running more than one probe at a time, it's recommended to separate their execution to avoid potential issues when both probes aim at using the Zabbix Agent at the same time. Modify this configuration according to your needs, so the probes will run when required, but bear in mind that running them in parallel may create issues with the Zabbix Agent, since it is not ready to work with concurrent tasks.

4 Configuration
----------------- 

The OCCI probe requires to modify the occiprobe.properties file in order to set the following parameters:
* openstack.user - Set here the user to be used for accessing OCCI APIs
* openstack.password - Set here the password to be used for accessing the OCCI APIs
* java.keystore - Set here the full location of the security certificates keystore
* zabbix.ip - Provide the IP address of the Zabbix server where metrics will be sent
* zabbix.sender.location - Configure the location where the Zabbix agent was installed, indicating the zabbix sender path
* cmdb.location - Provide the full URL of the CMDB component, providing the information about the available providers


5 Packages Update
-------------------
If a previous version of the packages is already installed, it is necessary to update them to the new version. That can be done through the following commands.

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/occi-zabbix-probe-1.01.deb
```
```
dpkg -i occi-zabbix-probe-1.01.deb
```

* CentOS
```
sudo rpm -Uvh https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/OCCIZabbixProbe-1.01.rpm
```

6 Potential Issues
--------------------
In the case of providers requiring some communication using SSL, if the provider certificate is not signed by a known entity, the JVM may throw exceptions. In such case, it is necessary to register the corresponding certificate with the following command:
```
keytool -importcert -trustcacerts -alias infnkeystone -file infnkeystone.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts"
```
