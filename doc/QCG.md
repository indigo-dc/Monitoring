# 1 Main Features

This probe works by getting from the list of available providers the one responsible for QCG (QosCosGrid) and sends the values for selected metrics to the Zabbix server via teh Zabbix Wrapper.

# 2 Pre-Requisites

Not fulfilling these requisites will have a negative impact in the execution of the probes.

 * It requires a CMDB available, so it will be possible to retrieve the list of available providers. Having no access to a CMDB means that the probe will not be able to retrieve a list of providers to monitor, therefore not doing anything;
 * Since the implementation of the probe is in Java, it requires, Java8 JVM to be already installed.

# 3 Installation

## 3.1 Ubuntu 16.04
  1. You have to enable the INDIGO - DataCloud packages repositories. See full instructions
  [here](https://indigo-dc.gitbooks.io/indigo-datacloud-releases/content/generic_installation_and_configuration_guide_2.html#id4).
  2. After that you need to install the Java 8 Runtime Environment with
  ```
  # sudo apt install openjdk-8-jre-headless
  ```
  3. Finally you can install the probe with the command
  ```
  # wget https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/qcg-zabbix-probe/qcg-zabbix-probe-1.0_all.deb
  # sudo apt install qcg-zabbix-probe
  ```

## 3.2 Centos 7
  1. You have to enable the INDIGO - DataCloud packages repositories. See full instructions
  [here](https://indigo-dc.gitbooks.io/indigo-datacloud-releases/content/generic_installation_and_configuration_guide_2.html#id4).
  2. After that you need to install the Java 8 Runtime Environment with
  ```
  # sudo yum install java-1.8.0-openjdk-headless
  ```
  3. Finally you can install the probe with the command
  ```
  # wget https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/qcg-zabbix-probe/qcg-zabbix-probe-1.0_all.deb
  # sudo yum install qcg-zabbix-probe
  ```

## 3.3 Cron Job
Although the probes can be run just on demand, the best option is to configure them as Cron jobs. That can be configured by editing the configuration file with the following command:
```
# crontab -e
```

Then, add the following lines:
```
0 * * * * java -jar /usr/share/java/zabbix/qcg-zabbix-probe-1.0-jar-with-dependencies.jar
```

This means that the probe will run every hour at xx:00. Modify this configuration according to your needs, so the probes will run when required.

# 4 Configuration

The QCG probe requires to modify the qcgprobe.properties (inserted in /etc/zabbix/ for linux systems in C:\zabbixconfig\ for Windows ones) file in order to set the following parameters:

* cmdb.location - Provide the full URL of the CMDB component, providing the information about the available providers
* iam.location - Endpoint of IAM
* iam.username - username in the IAM
* iam.password - pasword
* iam.clientid - Client ID obtained from IAM for registered QCG probe
* iam.clientsecret - Client password for QCG probe
* iam.grant_type - Grant type set for QCG probe in the IAM (e.g. client_credentials)
* zabbix.wrapper.location - Zabbix wrapper URL
* zabbix.ip - Zabbix IP
* qcg.metrics - Metrics the user wants to gather. Currently one of the values: qcg.api.read.availability, qcg.api.read.cpus.total, qcg.api.read.cpus.used

