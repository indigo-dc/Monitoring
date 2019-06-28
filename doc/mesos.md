1 Main Features
------------------
The mesos probe has three different components:

* Mesos: It will check the health status of the Mesos cluster by accessing the metrics that it exposes
* Chronos: It will check the correct behaviour of the cluster by checking that it can create a job, run it and then delete it.
* Marathon: It will check the correct behaviour of the cluster by checking that it can create a application, run it and then delete it.

2 Pre-Requisites
------------------

* It is necessary to have a Mesos instance running with the API exposed. The same is applicable to Marathon and Chronos in case the monitoring of them is needed.
* It requires an instance of Zabbix which it can send the metrics collected.
* It requires an instance of CMDB to get the providers that the probe must test

Since the implementation of the probes is in Java, it requires, at least, a Java8 JVM to be already installed.

3 Installation
----------------

First of all, make sure the JRE is installed. If this is not the case, they can be installed following a few simple steps.

In order to install the JRE:
* Ubuntu (16.04):
```
sudo add-apt-repository ppa:openjdk-r/ppa
sudo apt-get update
sudo apt-get install openjdk-8-jre-headless
```

* CentOS:
```
sudo yum install java-1.8.0-openjdk-headless
```

Then, it is necessary to install the corresponding packages generated for the probes.

* Ubuntu:
```
wget https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/mesos-zabbix-probe/mesos-zabbix-probe_1.4_all.deb
```
```
dpkg --install mesos-zabbix-probe-1.4.deb
```

* CentOS
```
sudo yum install https://github.com/indigo-dc/Monitoring/raw/master/zabbix-probes/mesos-zabbix-probe/MesosZabbixProbe-1.4.noarch.rpm
```

4 Running
----------------

Running of the probes is accomplished by executing the `mesosprobe.sh` command with the arguments `mesos`, `marathon` or `chronos` to send the metrics corresponding to each product. So for example running `mesosprobe.sh mesos` will check the metrics on the configured Mesos cluster and send them to the also configured Zabbix server.

Although the probes can be run just on demand, the best option is to configure them as Cron jobs. That can be configured by editing the configuration file with the following command:
```
crontab -e
```

Then, add the following lines:
```
00 * * * * mesosprobe.sh mesos
15 * * * * mesosprobe.sh chronos
30 * * * * mesosprobe.sh marathon
```

This means that the probe will run every hour at xx:00, xx:15 and xx:30 each one monitoring one aspect of the cluster and conveniently separated to avoid conflicts when sending data.

5 Configuration
----------------- 

The configuration of the different parameters is accomplished by modifying the /etc/zabbix/mesosprobe.properties file:

To change the location of the configuration file, you can provide a `-l <location>` argument to the launch script so for example, executing `mesosprobe.sh mesos -l /home/user` will try to find the configuration file at `/home/user/mesosprobe.properties`.

* cmdb.location: URL pointing to a CMDB instance to get Mesos, Chronos and Marathon services locations.

* iam.location: Location of the IAM instance.
* iam.username: Username of the IAM to use to get data and create objects.
* iam.password: Password associated to the above use.
* iam.clientid: ClientID to use to authenticate against the IAM instance.
* iam.clientsecret: Client secret of the above ClientID.

* hosts.category: Hosts will be registered under this category. Defaults to "IAAS"

* zabbix.wrapper.location: Location of the Zabbix wrapper instance that will serve to register hosts dynamically
* zabbix.ip: IP of the Zabbix server to send the metrics
* zabbix.port: **Optional.** Port of the Zabbix server. If undefined it will use the default 10051

* mesos.metric: **This property can be defined more than once.** Each value of this property will be a metric retrieved from the metrics API that will be sent to the Zabbix server. Since those metrics come in the form `master/<metric>` it will be sent to Zabbix as `master.<metric>` key. Please make sure the key is correctly defined in the template associated to the host in the Zabbix configuration.

The default Mesos template has this properties that must be in the configuration file:

```
mesos.metric=master/uptime_secs
mesos.metric=master/tasks_lost
mesos.metric=master/slaves_active
mesos.metric=master/cpus_percent
mesos.metric=master/elected
mesos.metric=master/dropped_messages
mesos.metric=master/frameworks_inactive
mesos.metric=master/frameworks_disconnected
mesos.metric=master/disk_total
mesos.metric=master/disk_used
mesos.metric=master/disk_percent
mesos.metric=master/gpus_total
mesos.metric=master/gpus_used
mesos.metric=master/gpus_percent
mesos.metric=master/mem_total
mesos.metric=master/mem_used
mesos.metric=master/mem_percent
mesos.metric=master/cpus_total
mesos.metric=master/cpus_used
mesos.metric=master/cpus_percent
```

As for logging, the probe uses the default JDK 1.4 logging system through Apache Commons Logging. As such, a default configuration is provided in the file /etc/zabbix/mesosprobe-log.properties that will log events by default to the console and the /var/log/mesosprobe<number>.log file

6 Docker
----------------- 

Build a docker image with ```docker build . -t <image_name>:<image_version>``` where ```image_name``` and ```image_version``` are arbitrary names although something like ```mesos-probe:1.4``` is recommended

A container that executes the probe can then be run with ```docker run -v <config_path>:/etc/zabbix <image_name>:<image_version> <probe>``` where ```config_path``` is a path in the local filesystem to a folder containing a valid ```mesosprobe.properties``` file and ```probe``` is the probe to execute (```mesos```, ```chronos``` or ```marathon```)