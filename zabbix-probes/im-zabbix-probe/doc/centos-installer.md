# Installers from packages

## Centos 7

To build the package (if needed or if you want to make some changes) get the rpmbuild-imzabbix-1.0-Centos7.tar.gz, uncompress it and run the next command from the rpmbuild directory:

```sh
rpmbuild -v -bb --clean SPECS/imzabbix-agent.spec
```

Check into RPMS/x86_64/ that imzabbix-agent-1.0-1.el7.centos.x86_64.rpm is generated.

To install the package run:

```sh
rpm -ivh imzabbix-agent-1.0-1.el7.centos.x86_64.rpm
```

The software will be installed at /opt/imzabbix-agent-1.0

The file get-access-token.sh is a useful tool to get an access_token and a token_refresh from IAM, giving the user and password for IAM, and client ID/client secret credentials; this can be done with the grant type password. Replace the label with the respective value into the script.

```sh
IAM_USER='__IAM_USER__'
IAM_PASSWORD='__IAM_PASSWORD__'

client_secret='__CLIENT_SECRET__'
client_id='__CLIENT_ID__'
```

Then launch it.

```sh
sh get-access-token.sh
```

You can obtain a response like it follows:
```
{"access_token":"eyJraWQiOiJy...c2qXq_Vo","token_type":"Bearer","refresh_token":"eyJhbGci...Tc2N2YifQ.",
"expires_in":3599,"scope":"address phone openid email profile offline_access","id_token":"eyJraWQi...vmYH_o"}
```
The main script to execute the imzabbix agent is probeim.py. This script needs some information to communicate with IAM, with Infrastructure Manager (IM) and with Zabbix. 

Assuming that your zabbix user is "Admin" and password is "zabbix", from shell you can execute the agent like it follows:

```sh
python probeim.py -i $CLIENT_ID -s $CLIENT_SECRET -t $TOKEN -r $REFRESH -u Admin -p zabbix -v $ZABBIX_SERVER -a $ZABBIX_API_URL
```

For an easy use, the script setimzabbixvar.sh sets this variables and launch the command, so you can configure this script by replacing the labels by the respective values. 

```sh
CLIENT_ID=__CLIENT_ID__
CLIENT_SECRET=__CLIENT_SECRET__
TOKEN=__IAM_TOKEN__
REFRESH=__REFRESH_TOKEN__
ZABBIX_SERVER=__ZABBIX_SERVER_IP__
ZABBIX_API_URL=http://__ZABBIX_WEB_IP__/api_jsonrpc.php
```
