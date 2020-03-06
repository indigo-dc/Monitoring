#!/usr/bin/env bash

COMMAND=$1
LOG_CONF="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Jdk14Logger -Djava.util.logging.config.file=/etc/zabbix/openstackprobe-log.properties"
BASE="java $LOG_CONF -cp /usr/share/java/zabbix/openstack-zabbix-probe.jar"

<<<<<<< HEAD
$BASE org.indigo.openstackprobe.openstack.QCGThread ${@:2}

=======
$BASE org.indigo.openstackprobe.openstack.QCGThread ${@:2}
>>>>>>> 7a7f9f0dc506fbe57b710ea94cfaad2e119afe23
