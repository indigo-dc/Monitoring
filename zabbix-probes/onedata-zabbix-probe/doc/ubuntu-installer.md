# Installers from packages

To install the package run:

```sh
Ubuntu 16
dpkg -i onedatazabbix-agent-1.0-Ubuntu16.deb

Ubuntu 14
dpkg -i onedatazabbix-agent-1.0-Ubuntu16.deb
```

The software will be installed at /opt/onedatazabbix

The file get-access-token.sh is a useful tool to get an access_token and a token_refresh from IAM, giving the user and password for IAM, and client ID/client secret credentials; this can be done with the grant type password. Replace the label with the respective value into the script.


```sh
IAM_USER='__IAM_USER__'
IAM_PASSWORD='__IAM_PASSWORD__'
-d client_id=__CLIENT_ID__ \
-d client_secret=__CLIENT_SECRET__ \
```

Then launch it.

```sh
sh get-access-token.sh
```

You can obtain a response like it follows:
```sh
{"access_token":"eyJraWQiOiJy...c2qXq_Vo","token_type":"Bearer","refresh_token":"eyJhbGci...Tc2N2YifQ.","expires_in":3599,"scope":"address phone openid email profile offline_access","id_token":"eyJraWQi...vmYH_o"}
```

The main script to execute the onedatazabbix agent is onedata_zabbix_agent.py. This script needs some information to communicate with IAM and Zabbix.
Assuming that your zabbix user is "Admin" and password is "zabbix", from shell you can execute the agent like it follows:


```sh
python onedata_zabbix_agent.py -u=Admin -p=zabbix -t=$ONEDATA_TOKEN -i $CLIENT_ID -s $CLIENT_SECRET -r $REFRESH -a $ZABBIX_URL -v $ZABBIX_SERVER
```

For an easy use, the script setodbvars.sh sets this variables and launches the command, so you can configure this script by replacing the labels by the respective values.

```sh
CLIENT_ID=__YOUR_CLIENT_ID__
CLIENT_SECRET=__YOUR_CLIENT_SECRET__
ONEDATA_TOKEN=__YOUR_OBTAINED_TOKEN__
REFRESH=__TOKEN_REFRESH__
ZABBIX_URL=http://__IP_ZABBIX_WEB__/api_jsonrpc.php
ZABBIX_SERVER=__IP_ZABBIX_SERVER__
```
