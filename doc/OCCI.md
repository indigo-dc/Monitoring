1 Main Features
-----------------
This probe works by getting the list of available providers and, for each OCCI API available, a VM is created, inspected and deleted, as a way to confirm that the main operations to be done are working as expected in the provider under evaluation. The probe is able to monitor several providers concurrently and it sends all the gathered metrics to the Zabbix server collecting all the information.

2 Pre-Requisites
------------------
Not fulfilling these requisites will have a negative impact in the execution of the probes.

* A CMDB instance is suggested in order to monitor a series of providers from one single probe. However, extra information for every provider is needed as explained in the Configuration section.
* It requires providers exposing OCCI APIs, otherwise, it will not be possible to monitor anything.

Since the implementation of the probe is in Java, it requires, at least, a Java8 JVM to be already installed.

3 Installation
----------------

First of all, make sure to have a JRE 1.8 or newer available. If this is not the case, they can be installed following a few simple steps.

In order to install the JRE:
* Ubuntu 16.04:
```
sudo apt-get install default-jre
```

* CentOS:
```
sudo yum install java-1.8.0-openjdk
```

Then, it is necessary to install the corresponding packages:

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/occi-zabbix-probe-1.1.deb
```
```
dpkg --install occi-zabbix-probe-1.01.deb
```

* CentOS
```
sudo yum install https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/OCCIZabbixProbe-1.1.rpm
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

The OCCI probe works in two modes: integrated with the CMDB and as a standalone single provider probe. In any of them, the authentication information against the IAM is mandatory, so this elements are required:

- **iam.location:** URL pointing to a IAM instance that should provide tokens for the providers to monitor.
- **iam.username:** Username to use against the IAM. It should have enough privileges to launch Virtual Machines on every provider to monitor, as well as reading information about them.
- **iam.password:** Password for the aforementioned username
- **iam.clientid:** Client ID for the probe as registered in the IAM
- **iam.clientsecret:** Client secret to use for the client ID.

Also the usual Zabbix location properties should be present:

- **zabbix.ip:** Location to the Zabbix server to send the metrics to
- **zabbix.wrapper.location:** Location of the Zabbix wrapper instance to register hosts

###4.1 CMDB integration

When integrated with the CMDB the probe will get a list of providers and get the configuration information from the information returned with the query. To work in this mode, this configuration elements are needed:

- **cmdb.location:**: Location of a CMDB instance from which the probe will get a list of providers to monitor, as well as the configuration needed to do so.

In order to be able to do the monitoring in this mode, each provider returned by the CMDB must have the following configuration information in JSON:

```
oidc_config : {
    provider_id : "id of the IAM provider"
    protocol: "protocol to use"
}

occi_monitoring : {
    image_id : "Image id to use for monitoring",
    os_flavour: "Image flavour",
    network_id: "Optional network id to use during the monitoring"
} 
```
If this elements are not present, then the probe will skip the provider and not monitor it.

###4.2 Standalone

When a CMDB URL is not provided, the probe will act as a standalone monitor for one single provider. As such, the information of this provider is expected to be in the configuration as:

- **occi.provider:** Provider identifier to use
- **occi.endpoint:** Endpoint of the OCCI interface to monitor
- **keystone.endpoint:** Endpoint of the Keystone instance to log in
- **identity.provider:** The IAM provider ID to use in Keystone
- **iam.protocol:** The protocol part of the URL to request scoped tokens to Keystone
- **image.id:** The image to use for monitoring
- **os.flavour:** The image flavour to use
- **network.id:** Optional parameter to define a network id to use during monitoring

5 Packages Update
-------------------
If a previous version of the packages is already installed, it is necessary to update them to the new version. That can be done through the following commands.

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/occi-zabbix-probe-1.1.deb
```
```
dpkg -i occi-zabbix-probe-1.01.deb
```

* CentOS
```
sudo rpm -Uvh https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/occi-zabbix-probe/OCCIZabbixProbe-1.1.rpm
```

6 Potential Issues
--------------------
In the case of providers requiring some communication using SSL, if the provider certificate is not signed by a known entity, the JVM may throw exceptions. In such case, it is necessary to register the corresponding certificate with the following command:
```
keytool -importcert -trustcacerts -alias infnkeystone -file infnkeystone.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts"
```

When upgrading from version 1.01, please make sure that providers in the CMDB have the needed extra information. If that's not the case, then is better to change to standalone mode as explained in the Configuration section.
