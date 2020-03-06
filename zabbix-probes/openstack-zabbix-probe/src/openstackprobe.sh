#!/usr/bin/env bash

COMMAND=$1
LOG_CONF="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Jdk14Logger -Djava.util.logging.config.file=/etc/zabbix/openstackprobe-log.properties"
BASE="java $LOG_CONF -cp /usr/share/java/zabbix/openstack-zabbix-probe.jar"

$BASE org.indigo.openstackprobe.openstack.QCGThread ${@:2}

