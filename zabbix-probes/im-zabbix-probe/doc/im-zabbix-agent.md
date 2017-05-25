# IM-ZABBIX AGENT

## 1	INTRODUCTION

Infrastructure Manager (IM) is a tool that eases access and usability of IaaS clouds by automating virtual machine infrastructure selection, deployment, configuration, software installation, monitoring and update of virtual appliances.  
In order to offer relevant data to users about the health of its operations, the most relevant metrics are obtained by a python based monitor agent, using the IM REST API. Once obtained data from platform, this content is processed and sent to a Zabbix server which store and visualize monitorization data. 

## 2	ARCHITECTURE

The monitorization agent is a client-side software, that connects with IM to capture monitorization data, and sends it to Zabbix server. Agent does not need any open ports, just send data on passive mode to Zabbix and gather data from IM via IM API REST by GET, POST, PUT and DELETE http requests. Agent is composed by scripts that use IM API and Zabbix API, and loads configurations from files that must be edited correctly by an administrator.

## 3	FILE STRUCTURE

The agent is composed by a principal python script named probeim.py, four support libraries named loadconfigs.py, tokenmng.py, IMinfrastructureOper.py and zabbix.py and a directory conf with two configuration files, imzabbix.conf and authorizationHeader.txt.

```sh
├── conf
│   ├── imzabbix.conf
│   └── authorizationHeader.txt
├── loadconfigs.py
├── probeim.py
├── IMinfrastructureOper.py
├── tokenmng.py
└── zabbix.py
```

### 3.1	SCRIPTS

* File [probeim.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/probeim.py) is the executable script that gathers all measured data and sends it to zabbix. 
* File [zabbix.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/zabbix.py) contains functionalities related to interaction with zabbix API. Here we can authenticate against zabbix obtaining a valid token; get a list of all monitored items; create items; get template and item identifiers. 
* File [IMinfrastructureOper.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/IMinfrastructureOper.py) contains functionalities related to interaction with IM environment through its API. Here we can throw actions like creating infrastructure, listing infrastructure, creating VM, deleting infrastructure, and get the status of each of these actions. 
* File [tokenmng.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/tokenmng.py) has functions that connect with IAM to request a token from a refresh token, so client remains authenticated to be able to launch operations over IM. 
* File [loadconfigs.py](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/loadconfigs.py) contains classes that help to bring configuration data to the agent and are used to connect to Zabbix, IAM and IM.

### 3.2	CONFIGURATION

* imzabbix.conf

Example from /conf [here](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/conf/imzabbix.conf)


| Section| zabbix|
| ------ | ------ |
| uri | the api url to make requests |
| username| the zabbix username used for authentication in order to get a token |
| password | the zabbix password used for authentication |
| server_add | IP address where zabbix server is running |
| agent_delay| Time in seconds for each loop that catches data |
| template | the name of template used for IM measurements |
| monitoredhost | the hostname of the monitored host |


| Section | im |
| ------ | ------ |
| urlbase	| the IM api url to make requests |
| radl |	the radl configuration for IM to create a VM. |


| Section | iam |
| ------ | ------ |
| urlrefresh	| the api url to request a new token from a refresh token|


| Section | log |
| ------ | ------ |
| loglevel |	the severity of logs. This can take the following values: ERROR, WARNING, INFO, DEBUG
Default values is WARNING. |

* authorizationHeader.txt

Example from /conf [here](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/conf/authorizationHeader.txt)

## 4	DATA SOURCES

| METHOD /URL| Items|
| ------ | ------ |
| GET /infrastructures| List_inf |
| POST /infrastructures| Create_inf |
| PUT /infrastructures/<infId>/start | Start_inf |
| POST /infrastructures/<infId>
 body:	RADL document | Create_vm |
| DELETE /infrastructures/<infId|Delete_inf |


To connect to IM an appropriated header must be set.
```sh
HEADERS = {
        "Content-Type" : "text/plain",
        "Accept": "application/json",
        "Authorization" : authoriz
    }
 ```
Where the authoriz field comes from [authorizationHeader.txt](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/conf/authorizationHeader.txt) file.

## 5	DOCKER CONTAINER

Create or download a [Dockerfile](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/Dockerfile)  Use your Docker user and a name you want for your image.

Download and execute the [script](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/get-access-token.sh) used to request to IM an access token. You need your client credentials, IAM user and password.

```sh
[root@localhost imzabbix]# sh get-access-token.sh 

{"access_token":"eyJraWQiOiJyc2E...","token_type":"Bearer","refresh_token":"eyJhbGciOiJub25lIn0.eyJqd...","expires_in":3599,"scope":"address phone openid email profile offline_access","id_token":"eyJraWQiOi..."}
[root@localhost imzabbix]# 

```
Build an image from the directory where your Dockerfile is located.

```sh
[root@localhost imzabbix]# ls Dockerfile 
Dockerfile
[root@localhost imzabbix]# docker build -t <dockeruser>/<my_built_image> .
Sending build context to Docker daemon 692.2 kB
Step 1 : FROM centos
 ---> 0584b3d2cf6d
...
Successfully built afe963948e10
[root@localhost imzabbix]# 

```

After image was created, set up required parameters and run the docker container.

```sh
[root@localhost imzabbix]# ZABBIX_PASSWORD=zabbix
[root@localhost imzabbix]# ZABBIX_SERVER=172.17.0.3
[root@localhost imzabbix]# ZABBIX_API_URL=http://172.17.0.4/api_jsonrpc.php
[root@localhost imzabbix]# ZABBIX_USER=Admin
[root@localhost imzabbix]# CLIENT_ID=d573b18e-7a59-4bed-bbbb-ebad2b5f8299
[root@localhost imzabbix]# CLIENT_SECRET=NxFfYvk19hiKuuLo50OO44nuwShRsV45UrvivWUwvF-4Szdadu2fuh7BwHLuttbZFeQYhYmEbbxdfSDrJajdNg
[root@localhost imzabbix]# TOKEN=eyJraWQiOiJyc2E...
[root@localhost imzabbix]# REFRESH=REFRESH=eyJhbGciOiJub25lIn0.eyJqd...
[root@localhost imzabbix]#
[root@localhost imzabbix]# docker run --name MY_IMZABBIX_CONTAINER -e CLIENT_ID=$CLIENT_ID -e CLIENT_SECRET=$CLIENT_SECRET -e TOKEN=$TOKEN -e REFRESH=$REFRESH -e ZABBIX_USER=$ZABBIX_USER -e ZABBIX_PASSWORD=$ZABBIX_PASSWORD -e ZABBIX_SERVER=$ZABBIX_SERVER -e ZABBIX_API_URL=$ZABBIX_API_URL -d <dockeruser>/<my_built_image>

```

