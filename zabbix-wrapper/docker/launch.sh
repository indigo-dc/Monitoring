#!/bin/bash

#IPADDR=$(ip a s | sed -ne '/127.0.0.1/!{s/^[ \t]*inet[ \t]*\([0-9.]\+\)\/.*$/\1/p}')
ZONE_CONFIG_FILE="$JBOSS_HOME/standalone/deployments/$WAR_NAME.war/WEB-INF/classes/zones.yml"
sed -i "s/^\( *url: \).*$/\1$(echo $ZABBIX_URL | sed -e 's/[\/&]/\\&/g')/" ${ZONE_CONFIG_FILE};
sed -i "s/^\( *username: \).*$/\1$(echo $ZABBIX_USERNAME | sed -e 's/[\/&]/\\&/g')/" ${ZONE_CONFIG_FILE};
sed -i "s/^\( *password: \).*$/\1$(echo $ZABBIX_PASSWORD | sed -e 's/[\/&]/\\&/g')/" ${ZONE_CONFIG_FILE};

if [ "${ENABLE_DEBUG}" = "true" ];
	then DEBUG_ARG="--debug";
	else DEBUG_ARG="";
fi

CLUSTER_MESSAGING_PASSWORD="pwd"
$JBOSS_HOME/bin/standalone.sh -c $JBOSS_CONF_FILE -Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0