#!/bin/bash
echo "Copying configuration files to $OS_PROBE_CONF"

#import the certificate
keytool -import -trustcacerts -keystore /usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts  -storepass changeit -alias infnCertBa -file /etc/zabbix/cacertBari.cer

#Configure the cronjob 
echo "*/10 * * * * java -jar /root/openstack-zabbix-probe-1.01-jar-with-dependencies.jar" >> /var/spool/cron/crontabs/root
/bin/bash
