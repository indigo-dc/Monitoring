#!/usr/bin/env bash

COMMAND=$1
BASE=java -cp /usr/share/java/zabbix/mesos-zabbix-probe.jar

case $COMMAND in
  mesos)
    $BASE com.indigo.mesosprobe.MesosThread
    ;;

  marathon)
    $BASE com.indigo.mesosprobe.marathon.MarathonThread
    ;;

  chronos)
    $BASE com.indigo.mesosprobe.chronos.ChronosThread
    ;;

  *)
    echo "Unrecognized option $COMMAND"
    ;;
esac