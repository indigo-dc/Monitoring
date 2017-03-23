#!/bin/bash
echo "Copying configuration files to $OS_PROBE_CONF"
#Configure the cronjob 
echo "*/10 * * * * java -jar /root/openstack-zabbix-probe-1.01-jar-with-dependencies.jar" >> /var/spool/cron/crontabs/root
/bin/bash
