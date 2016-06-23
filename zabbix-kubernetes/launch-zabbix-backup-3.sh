#!/bin/bash
# Basic Script to Backup Zabbix MySQL Databases

# It is needed the Kubernetes Client installed and present in the PATH.

set -e
set -x

# Kubernetes Configuration
KUBE_USER=[changeme]
KUBE_TOKEN=[changeme]
KUBE_NAMESPACE=[changeme]
KUBE_SERVER=[changeme]
KUBE_IGNORE_TLS=[changeme]

# Zabbix Pod Configuration
KUBE_APP=zabbix
KUBE_RC=zabbix-db

# Backup Destination Configuration
BCK_DST=/backups
BCK_DST_LOCAL=/backups-zabbix

# Geenration of the destination localhost
if [ ! -d $BCK_DST_LOCAL ]
then
	mkdir -p $BCK_DST_LOCAL
	if [ $? -ne 0 ]
	then 
		echo "Error, cannot create $BCK_DST_LOCAL"
		exit 255
	fi
fi

# Get zabbix-db POD namespace
POD_DB_ZABBIX=$(kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN"  --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE get pods --selector=app=$KUBE_APP -o=jsonpath="{.items[?(@.metadata.labels.name==\"$KUBE_RC\")].metadata.name}")

#Take the Config Backup
echo "Taking Backup of the Configuraton"
BCK_OUT_CFG=$(kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN"  --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE exec $POD_DB_ZABBIX -- /zabbix-backup/zabbix-mariadb-dump -u zabbix -p my_password -o $BCK_DST | grep zabbix_cfg_localhost_)
BCK_OUT_CFG=${BCK_OUT_CFG#*$BCK_DST/} #Remove the '/$BCK_DST/' string.

#Get the Backup from the POD
echo "Downloading Backup $BCK_DST/$BCK_OUT_CFG to localhost"
kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN" --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE exec $POD_DB_ZABBIX -- /bin/cat $BCK_DST/$BCK_OUT_CFG > $BCK_DST_LOCAL/$BCK_OUT_CFG

#Delete the Backup from the POD
echo "Deleting remote Backup of: $BCK_DST/$BCK_OUT_CFG"
kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN" --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE exec $POD_DB_ZABBIX -- /bin/rm -rf $BCK_DST/$BCK_OUT_CFG

#Take the Data Backup
BCK_OUT_DATA=zabbix_db_dump_$(date +%Y%m%d-%H%M.%S).sql.bz2
echo "Taking Backup of the Data"
kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN"  --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE exec  $POD_DB_ZABBIX -- /bin/bash -c "mysqldump -u zabbix -pmy_password zabbix | bzip2 -cq9 > $BCK_DST/$BCK_OUT_DATA"

#Get the Backup from the POD
echo "Downloading Backup $BCK_DST/$BCK_OUT_DATA to localhost"
kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN" --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE exec $POD_DB_ZABBIX -- /bin/cat $BCK_DST/$BCK_OUT_DATA > $BCK_DST_LOCAL/$BCK_OUT_DATA

#Delete the Backup from the POD
echo "Deleting remote Backup of: $BCK_DST/$BCK_OUT_DATA"
kubectl -s $KUBE_SERVER --user="$KUBE_USER" --token="$KUBE_TOKEN" --insecure-skip-tls-verify=$KUBE_IGNORE_TLS --namespace=$KUBE_NAMESPACE exec $POD_DB_ZABBIX -- /bin/rm -rf $BCK_DST/$BCK_OUT_DATA