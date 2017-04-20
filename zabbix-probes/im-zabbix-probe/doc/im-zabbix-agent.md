# 1	IM-ZABBIX AGENT

## 1.1	INTRODUCTION

Infrastructure Manager (IM) is a tool that eases access and usability of IaaS clouds by automating virtual machine infrastructure selection, deployment, configuration, software installation, monitoring and update of virtual appliances.  
In order to offer relevant data to users about the health of its operations, the most relevant metrics are obtained by a python based monitor agent, using the IM REST API. Once obtained data from platform, this content is processed and sent to a Zabbix server which store and visualize monitorization data. 

## 1.2	ARCHITECTURE

The monitorization agent is a client-side software, that connects with IM to capture monitorization data, and sends it to Zabbix server. Agent does not need any open ports, just send data on passive mode to Zabbix and gather data from IM via IM API REST by GET, POST, PUT and DELETE http requests. Agent is composed by scripts that use IM API and Zabbix API, and loads configurations from files that must be edited correctly by an administrator.

## 1.3	FILE STRUCTURE

The agent is composed by a principal python script named probeim.py, four support libraries named loadconfigs.py, tokenmng.py, IMinfrastructureOper.py and zabbix.py and a directory conf with two configuration files, imzabbix.conf and authorizationHeader.txt.

### 1.3.1	SCRIPTS

File probeim.py is the executable script that gathers all measured data and sends it to zabbix. 
File zabbix.py contains functionalities related to interaction with zabbix API. Here we can authenticate against zabbix obtaining a valid token; get a list of all monitored items; create items; get template and item identifiers. 
File IMinfrastructureOper.py contains functionalities related to interaction with IM environment through its API. Here we can throw actions like creating infrastructure, listing infrastructure, creating VM, deleting infrastructure, and get the status of each of these actions. 
File tokenmng.py has functions that connect with IAM to request a token from a refresh token, so client remains authenticated to be able to launch operations over IM. 
File loadconfigs.py contains classes that help to bring configuration data to the agent and are used to connect to Zabbix, IAM and IM.

### 1.3.2	CONFIGURATION

* imzabbix.conf

Example from /conf [here](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/conf/imzabbix.conf)

In zabbix section:	
| Uri|	the api url to make requests|
| Username|	the zabbix username used for authentication in order to get a token|
| Password|	the zabbix password used for authentication|
| server_add|	IP address where zabbix server is running|
| agent_delay |	Time in seconds for each loop that catches data|
| Template |	the name of template used for IM measurements|
| monitoredhost |	the hostname of the monitored host|

In im section	
| Urlbase	|the IM api url to make requests|
| Radl|	the radl configuration for IM to create a VM.|

In iam section	
| urlrefresh	|the api url to request a new token from a refresh token|

In log section	
| Loglevel|	the severity of logs. This can take the following values: ERROR, WARNING, INFO, DEBUG
Default values is WARNING. |

* authorizationHeader.txt

Example from /conf [here](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/conf/authorizationHeader.txt)

## 1.4	DATA SOURCES

| METHOD /URL| Items|
| ------ | ------ |
| GET /infrastructures| List_inf |
| POST /infrastructures| Create_inf |
| PUT /infrastructures/<infId>/start | Start_inf |
| POST /infrastructures/<infId>
 body:	RADL document | Create_vm |
| DELETE /infrastructures/<infId|Delete_inf |


To connect to IM an appropriated header must be set.
| HEADERS = {
        "Content-Type" : "text/plain",
        "Accept": "application/json",
        "Authorization" : authoriz
    } |

Where the authoriz field comes from [authorizationHeader.txt](https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/im-zabbix-probe/conf/authorizationHeader.txt) file.

## 1.5	EXECUTING THE AGENT

The main script is probeim.py and this needs (mandatory) a client id ('-i','--client_id), the client secret ('-s','--client_secret),  a token ('-t','--token) and a refresh token ('-r','--token_refresh), given by the IAM for authentication purposes. 

|  python probeim.py -i $CLIENT_ID -s $CLIENT_SECRET -t $TOKEN -r $REFRESH |

## 1.6	DOCKER CONTAINER

Download and extract the IM-zabbix agent directory (e.g. ./imzabbix). Create a Dockerfile at the same level of agent directory, and run the command to build the docker image. Use your Docker user and a name you want for your image (don’t forget the dot at the end).  

After image was created, run the docker container.
