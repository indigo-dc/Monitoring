# ONEDATA-ZABBIX AGENT

## 1 INTRODUCTION
Onedata is a high performance data management solution that offers data access across distributed environments. With Onedata, users can access, store, process and publish data using global data storage backed by storage providers worldwide. All data stored in Onedata is organized into spaces, which can be seen as virtual folders that can contain an arbitrary file hierarchy while being distributed across multiple storage providers. Each space has to be supported by at least one provider; this provider has reserved certain storage quota for this particular space. If a space is supported by more than one provider, the total quota is the sum of storage spaces provisioned by all providers.  Onedata as a distributed system is divided into zones that are created by deploying a dedicated service called onezone. 
In order to offer relevant data to users, providers and administrators about spaces, users and groups, the most relevant metrics are obtained by a python based monitor agent, using the onedata REST API. Once obtained data from platform, this content is processed and sent to a Zabbix server which store and visualize monitorization data. 


## 2	ARCHITECTURE

The monitorization agent is a client-side software, that connects with ONEDATA server to capture monitorization data, and sends it to Zabbix server. Agent does not need any open ports, just send data on passive mode to Zabbix and gather data from ONEDATA via API REST by GET requests. Agent is composed by scripts that use ONEDATA API and Zabbix API, and loads configuration from a file that must be edited correctly by an administrator.

## 3 FILE STRUCTURE
The agent is composed by a principal python script named onedata_zabbix_agent.py, three support libraries named loadconfigs.py, onedata.py and zabbix.py, a directory conf with three configuration files, onedataconf.json, zabbix_items.json and zabbix.cfg.

```sh
├── conf
│   └── onedata-zabbix.cfg
├── loadconfigs.py
├── onedata.py
├── onedata_zabbix_agent.py
└── zabbix.py
```
### 3.1. SCRIPTS

* File [onedata_zabbix_agent.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/onedata_zabbix_agent.py)  is the executable script that gathers all measured data and sends it to zabbix. 
* File [zabbix.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/zabbix.py)  contains functionalities related to interaction with zabbix API. Here we can authenticate against zabbix obtaining a valid token; get a list of all monitorized items; get a list of groups in which we can organize items to visualize them on a good way on web interface; create items; get template and item identifiers. 
* File [onedata.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/onedata.py) contains functionalities related to interaction with onedata environment through its API.  Here we can get list of spaces, list of groups by space, list of users by space, get measurable data from spaces, groups, users and providers from onezone and oneprovider components. A more detailed list of obtained data is exposed into zabbix_items.json. 
* File [loadconfigs.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/loadconfigs.py) contains classes that help to bring configuration data to the agent and are used to connecto to zabbix and onedata.

### 3.2. CONFIGURATION


* onedata-zabbix.conf

Example from /conf [here](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/conf/onedata-zabbix.cfg)


| Section| onedata|
| ------ | ------ |
| portZone | the port of onezone host to make requests |
| portProvider| the port of oneprovider to make requests |
| hostZone | the url or ip address that agent can take to build an url that is used to get measurement data related to providers (oneprovider component) |
| hostProvider | the url or ip address that agent can take to build an url that is used to get measurement data related to users and groups from each space (onezone component) |


| Section| onedata|
| ------ | ------ |
| agent_delay| Time in seconds for each loop that catches data |
| template | the name of template used for ONEDATA measurements |
| monitoredhost | the hostname of the monitored host |

Section items: this is a structure where each key is a group that gathers elements that are available for monitorization; its value can be 1 or 0, where 1 enables monitorization, 0 disables monitorization. The user used for monitorization has some privileges which let to take data from platform according to these. The elements that throw data can vary from user to user depending of its own privileges. 


| Section | iam |
| ------ | ------ |
| urlrefresh	| the api url to request a new token from a refresh token|


| Section | log |
| ------ | ------ |
| loglevel |	the severity of logs. This can take the following values: ERROR, WARNING, INFO, DEBUG
Default values is WARNING. |

## 4. DATA SOURCES

### 4.1. FROM ONEZONE

| Item | source | item | 
| ------ | ------ | ------ |
| onezone_spaces_namespace | GET /spaces/{id} | spaceId | 
| onezone_spaces_namespace_groups | GET /spaces/{id}/groups | groups |
| - | GET /spaces/{id}/groups | type, groupId | 
| - | GET /spaces/{id}/groups/{gid}/privileges | privileges | 
| onezone_spaces_namespace_providers | GET /spaces/{id}/providers | Providers | 
| - | GET /spaces/{id}/providers/{pid} | id, clientName, urls, longitude, latitude, redirectionPoint | 
| onezone_spaces_namespace_users | GET /spaces/{id}/users | Users | 
| - | GET /spaces/{id}/users/{uid} | name, privileges, userId | 

### 4.1. FROM ONEPROVIDER

| Item | source | item | 
| ------ | ------ | ------ |
| oneprovider_space_namespace_storage_quota | GET /metrics/space/{sid} | ?metric=storage_quota | 
| oneprovider_space_namespace_storage_used | GET /metrics/space/{sid} | ?metric=storage_used | 
| oneprovider_space_namespace_data_access | GET /metrics/space/{sid} | ?metric=data_access | 
| oneprovider_space_namespace_connected_users | GET /metrics/space/{sid} | ?metric=block_access | 


## 5. DATA INTO ZABBIX WEB INTERFACE

Before the agent starts to send data, the template must be created and configured in onedata-zabbix.conf.  

The name of the items is generated automatic and dynamically by the agent, is composed with the component (onezone, oneprovider), the word “space”, the name of the space, the item monitorized for onezone, one  word between “groups”, “provider” or “users” and the items or subitems inside each type of measurement, each word separated by a dot (“.”). 
For example, oneprovider.space.space1.storage_quota is the storage quota for space1 space from oneprovider. 

The type of data is set into the agent and depends of the nature of monitored item.

## 6	DOCKER CONTAINER

Create or download a [Dockerfile](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/Dockerfile)  Use your Docker user and a name you want for your image.

Download and execute the [script](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/onedata-zabbix-probe/get-access-token.sh) used to request to IM an access token. You need your client credentials, IAM user and password.

```sh
[root@localhost onedata]# sh get-access-token.sh 

{"access_token":"eyJraWQiOiJyc2Ex...","token_type":"Bearer","refresh_token":"eyJhbGciOi...","expires_in":3599,"scope":"address email profile offline_access"}

[root@localhost onedata]# 

```
Build an image from the directory where your Dockerfile is located.

```sh
[root@localhost onedata]# ls Dockerfile 
Dockerfile
[root@localhost onedata]# docker build -t <dockeruser>/<my_built_image> .
Sending build context to Docker daemon 692.2 kB
Step 1 : FROM centos
 ---> 0584b3d2cf6d
...
Successfully builtn 301cfde12ffe
[root@localhost onedata]# 

```

After image was created, set up required parameters and run the docker container.

```sh
[root@localhost onedata]# ZABBIX_PASSWORD=zabbix
[root@localhost onedata]# ZABBIX_SERVER=172.17.0.3
[root@localhost onedata]# ZABBIX_API_URL=http://172.17.0.4/api_jsonrpc.php
[root@localhost onedata]# ZABBIX_USER=Admin
[root@localhost onedata]# CLIENT_ID=4c61a040-d305-462...
[root@localhost onedata]# CLIENT_SECRET=EbbxdfSDrJajdNg
[root@localhost onedata]# TOKEN=eyJraWQiOiJyc2E...
[root@localhost onedata]# REFRESH=eyJhbGciOiJub25lIn0.eyJqd...
[root@localhost onedata]#
[root@localhost onedata]# docker run --name MY_ODZ_CONT -e CLIENT_ID=$CLIENT_ID -e CLIENT_SECRET=$CLIENT_SECRET -e ONEDATA_TOKEN=$ONEDATA_TOKEN -e REFRESH=$REFRESH -e ZABBIX_USER=$ZABBIX_USER -e ZABBIX_PASSWORD=$ZABBIX_PASSWORD -e ZABBIX_SERVER=$ZABBIX_SERVER -e ZABBIX_URL=$ZABBIX_URL -d <dockeruser>/<my_built_image>

```
