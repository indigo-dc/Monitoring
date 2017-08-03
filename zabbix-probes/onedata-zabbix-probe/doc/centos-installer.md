# Installers from packages

To build the package (if needed or if you want to make some changes) get the rpmbuild tar.gz pack, uncompress it and run the next command from the rpmbuild directory:

```sh
Centos 6
rpmbuild-onedatazabbix-1.0-Centos6.tar.gz

Centos 7
rpmbuild-onedatazabbix-1.0-Centos7.tar.gz
```

Make the build and check into RPMS/x86_64/ that rpm is generated.

```sh
rpmbuild -v -bb --clean SPECS/onedatazabbix-agent.spec
```

The Centos RPMs included in this repository are:

```sh
Centos 6
onedatazabbix-agent-1.0-1.el6.x86_64.rpm

Centos 7
onedatazabbix-agent-1.0-1.el7.centos.x86_64.rpm
```

To install the package run:

```sh
Centos 6
rpm -ivh onedatazabbix-agent-1.0-1.el6.x86_64.rpm

Centos 7
rpm -ivh onedatazabbix-agent-1.0-1.el7.centos.x86_64.rpm
```

The software will be installed at /opt/onedatazabbix-agent-1.0

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
The main script to execute the OnedataZabbix agent is onedata_zabbix_agent.py. This script needs some information to communicate with IAM, with Onedata(onezone,oneprovider) and with Zabbix. 

Assuming that your zabbix user is "Admin" and password is "zabbix", from shell you can execute the agent like it follows:

```sh
python onedata_zabbix_agent.py -u=Admin -p=zabbix -t=$ONEDATA_TOKEN -i $CLIENT_ID -s $CLIENT_SECRET -r $REFRESH -a $ZABBIX_URL -v $ZABBIX_SERVER
```

For an easy use, the script set-onedatazabbix-vars.sh sets this variables and launches the main agent python script, so you can configure this script by replacing the labels by their respective values. 

```sh
CLIENT_ID=__YOUR_APP_CLIENT_ID__
CLIENT_SECRET=__YOUR_APP_CLIENT_SECRET__
ONEDATA_TOKEN=__IAM_TOKEN_OBTAINED__
REFRESH=__IAM_APP_REFRESH_TOKEN__
ZABBIX_URL=http://__IP_ZABBIX_WEB__/api_jsonrpc.php
ZABBIX_SERVER=__IP_ZABBIX_SERVER__
```
