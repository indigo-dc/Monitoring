# Installers from packages

## Ubuntu 16

To install the package run:

```sh
dpkg -i imzabbix-agent-1.0-Ubuntu-16.deb
```

The software will be installed at /opt/imzabbix

The file get-access-token.sh is a useful tool to get an access_token and a token_refresh from IAM, giving the user and password for IAM, and client ID/client secret credentials; this can be done with the grant type password. Replace the label with the respective value into the script.

```sh
IAM_USER='__YOUR_IAM_USER__'
IAM_PASSWORD='__YOUR_IAM_PASSWORD__'
IAM_CLIENT_SECRET='__YOUR_CLIENT_SECRET__'
IAM_CLIENT_ID='__YOUR_CLIENT_ID__'
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

From shell you can execute the agent like it follows:

```sh
python probeim.py -i $CLIENT_ID -s $CLIENT_SECRET -t $TOKEN -r $REFRESH -u $ZABBIX_USER -p $ZABBIX_PASSWORD -v $ZABBIX_SERVER -a $ZABBIX_API_URL
```

For an easy use, the script setimzabbixvar.sh sets this variables and launch the command, so you can configure this script by replacing the labels by the respective values. 

```sh
CLIENT_ID=__YOUR_CLIENT_ID__
CLIENT_SECRET=__YOUR_CLIENT_SECRET__
TOKEN=__YOUR_OBTAINED_TOKEN__
REFRESH=__TOKEN_REFRESH__
ZABBIX_SERVER=__IP_ZABBIX_SERVER__
ZABBIX_API_URL=http://__IP_ZABBIX_WEB__/api_jsonrpc.php
ZABBIX_USER=__YOUR_ZABBIX_USER__
ZABBIX_PASSWORD=__YOUR_ZABBIX_PASSWORD__
```
