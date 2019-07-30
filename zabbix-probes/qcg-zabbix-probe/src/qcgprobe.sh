#!/usr/bin/env bash

COMMAND=$1
LOG_CONF="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Jdk14Logger -Djava.util.logging.config.file=/etc/zabbix/qcgprobe-log.properties"
BASE="java $LOG_CONF -cp /usr/share/java/zabbix/qcg-zabbix-probe.jar"

$BASE com.deep.qcgprobe.QCGThread ${@:2}
