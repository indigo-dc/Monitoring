# 1	IM-ZABBIX AGENT

# 1.1	INTRODUCTION
Infrastructure Manager (IM) is a tool that eases access and usability of IaaS clouds by automating virtual machine infrastructure selection, deployment, configuration, software installation, monitoring and update of virtual appliances.  
In order to offer relevant data to users about the health of its operations, the most relevant metrics are obtained by a python based monitor agent, using the IM REST API. Once obtained data from platform, this content is processed and sent to a Zabbix server which store and visualize monitorization data. 

# 1.2	ARCHITECTURE
The monitorization agent is a client-side software, that connects with IM to capture monitorization data, and sends it to Zabbix server. Agent does not need any open ports, just send data on passive mode to Zabbix and gather data from IM via IM API REST by GET, POST, PUT and DELETE http requests. Agent is composed by scripts that use IM API and Zabbix API, and loads configurations from files that must be edited correctly by an administrator.

# 1.3	FILE STRUCTURE
The agent is composed by a principal python script named probeim.py, four support libraries named loadconfigs.py, tokenmng.py, IMinfrastructureOper.py and zabbix.py and a directory conf with two configuration files, imzabbix.conf and authorizationHeader.txt.

## 1.3.1	SCRIPTS
File probeim.py is the executable script that gathers all measured data and sends it to zabbix. 
File zabbix.py contains functionalities related to interaction with zabbix API. Here we can authenticate against zabbix obtaining a valid token; get a list of all monitored items; create items; get template and item identifiers. 
File IMinfrastructureOper.py contains functionalities related to interaction with IM environment through its API. Here we can throw actions like creating infrastructure, listing infrastructure, creating VM, deleting infrastructure, and get the status of each of these actions. 
File tokenmng.py has functions that connect with IAM to request a token from a refresh token, so client remains authenticated to be able to launch operations over IM. 
File loadconfigs.py contains classes that help to bring configuration data to the agent and are used to connect to Zabbix, IAM and IM.

## 1.3.2	CONFIGURATION

* imzabbix.conf

```javascript
[zabbix]
uri=http://172.17.0.4/api_jsonrpc.php
username=Admin
password=zabbix
server_add=172.17.0.3
template=imTemplateTest
monitoredhost=imhost
agent_delay=60

```
