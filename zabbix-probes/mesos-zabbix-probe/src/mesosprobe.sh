#!/usr/bin/env bash

COMMAND=$1
LOG_CONF="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Jdk14Logger -Djava.util.logging.config.file=/etc/zabbix/mesosprobe-log.properties"
BASE="java $LOG_CONF -cp /usr/share/java/zabbix/mesos-zabbix-probe.jar"

case $COMMAND in
  mesos)
    $BASE com.indigo.mesosprobe.MesosThread ${@:2}
    ;;

  marathon)
    $BASE com.indigo.mesosprobe.marathon.MarathonThread ${@:2}
    ;;

  chronos)
    $BASE com.indigo.mesosprobe.chronos.ChronosThread ${@:2}
    ;;

  *)
    echo "Unrecognized option $COMMAND"
    ;;
esac