This software layer has been written in order to expose Zabbix RESTful API and to work as adapter for different monitoring products

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

INDIGO zabbix-wrapper
============================

This is the monitoring wrapper of the PaaS layer, a component of the INDIGO project. It exposes REST API which wrap zabbix's JSON-RPC 2.0 API.
To make it properly work it should point to a zabbix server.



1. INSTALLATION
===============

1.1 REQUISITES
--------------

This project has been created with maven 3.3.3 and Java 1.8. Maven will take care of downloading the extra dependencies needed for the project.

1.2 INSTALLING
--------------

When having the war at disposal, starting from a clean VM with Ubuntu, install the docker manager:
```
sudo apt-get update
```
```
sudo apt-get install wget
```
```
sudo wget -qO- https://get.docker.com/ | sh
```

Install the application server (Wildfly 9.x) right from directory into which there is the docker file for giving the proper instructions and deploy the webapp
```
docker build -t indigodatacloud/zabbix-wrapper .
```
```
docker logs -f `sudo docker run --name zabbix-wrapper -e ZABBIX_URL=http://<url-zabbix>/api_jsonrpc.php -e ZABBIX_USERNAME=<zabbix-username> -e ZABBIX_PASSWORD=<zabbix-password> -p 80:8080 -d indigodatacloud/zabbix-wrapper`
```

The deploy will be successfull if the endpoints written in the property file are correct and the wrapper can reach the server itself

## In case wrapper is not a war --> Compile the code
To compile the project you need to be in the same folder as the `pom.xml` file and type:
```
mvn clean install -DskipTests
```
This command compiles the code and skip the tests. If you want to compile the code running the tests too you can use:
```
mvn clean install
```

At compilation completed, the `zabbix-wrapper.war` file will be put inside the `target` folder.

### Build the Docker image

The generated war must then be placed in the docker folder.

You can build the docker image with the command
```
docker build -t indigodatacloud/zabbix-wrapper /path/to/the/docker/folder
```

2. CONFIGURATION
===============
The only configuration needed for the project is concerned with the parameters to be passed when launching docker run command as the following:
 1. `ZABBIX_URL`: zabbix url in the format http://{domain-name}/api_jsonrpc.php
 2. `ZABBIX_USERNAME`: Zabbix username
 3. `ZABBIX_PASSWORD`: Zabbix password
 
3. API
===============
 
#### State of wrapper
```
GET: http://{ip:port}/monitoring/
 	RESPONSE Status 200 OK:
	<environment> - Zabbix wrapper, <version>
```
#### List of supported adapters
```
GET: http://{ip:port}/monitoring/adapters
 	RESPONSE Status 200 OK:
	{
  "result": {
         "adapters": [
     		"zabbix",
      		"<others>"
    		]		
}
```

#### List of zones where wrapper is working on
```
GET: http://{ip:port}/monitoring/adapters/zabbix/zones
RESPONSE Status 200 OK:
{"result":[
    "infn",
    "<others>",..
  ]
}
```

#### List of server types (depending on what servers are defined into the property file)
```
GET: http://{ip:port}/monitoring/adapters/zones/{zone}/{zabbix}/types
RESPONSE Status 200 OK:
{"result": {
    "servers": [
      "infrastructure",
      "service",
      "watcher"
    ]
 }
```
 
#### Groups list 
```
GET:http://{ip:port}/monitoring/adapters/{zabbix}/zones/{zone}/types/{serverType}/groups
 RESPONSE Status 200 OK:
{ "result": {
    		"groups": [
      			{
"groupName": "Zabbix Servers"
      			},
      			{
        		"groupName": "Linux servers"
      			},..
       ]
 	    }
}
```

#### Hosts list 
```
GET: http://{ip:port}/monitoring/adapters/{zabbix}/zones/{zone}/types/{serverType}/groups/{groupName}/hosts
RESPONSE Status 200 OK:
{
"result": {
    "groups": [
      {
        "groupName": "<hostGroupName>",
        "paasMachines": [
          {
            "machineName": "<hostName>",
            "ip": "<ip>",
            "serviceCategory": "<serviceCategory>",
            "serviceId": "<id>",
            "services": [
              {
                "serviceName": "<templateName>",
                "paasMetrics": [
                  {
                    "metricName": "<itemName>",
                    "metricKey": "key",
                    "metricValue":"<time>",
                    "metricUnit": "<unitMeasure>",
                    "paasThresholds": [],
                    "historyClocks": [],
                    "historyValues": []
                  },
                  }
                ]
              }
            ]
          },...}

```   
#### Group creation
```
POST: http://{ip:port}/monitoring/adapters/zabbix/zones/{zone}/types/{serverType}/groups/{groupName}/
  {
    	"hostGroupName":"<groupNameTobeSet>"
  }
RESPONSE Status 201 Created: 
  {
    "result": "<groupNameSet>",
  }
```

#### Host creation
```
PUT: http://{ip:port}/monitoring/adapters/{zabbix}/zones/{zone}/types/{serverType}/groups/{groupName}/hosts/{hostName}
{
  "ip":"<ip>",
  "serviceCategory":"appaas",
  "serviceId":"serviceId",
  "atomicServices": ["apache"],
  "uuid":"<uuid_to_insert>",
  "activeMode": true,
  "port":[
    {
     "portName":"APACHEPORT", 
           "value":"80"
    }
      ]
}
(activeMode e port  are optional parameters, if not mentioned in the body, default values will be associated)
 RESPONSE Status 201 Created: 
  {
   "result": "<hostNameSet>",
  }
```

#### Get host info
```
GET: http://{ip:port}/monitoring/adapters/{zabbix}/zones/{zone}/types/{serverTyp}/groups/{groupName}/hosts/{hostName}
RESPONSE Status 200 OK:
  {
    ** Same as hostGroup Response with one host description shown
  }
```
