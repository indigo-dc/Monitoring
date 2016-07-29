# User Guide

This guide aims at providing information which may be relevant to users of the probes and the Zabbix Wrapper. In the case of the Wrapper, it exposes a REST API which is described in this guide at a high level. On the other hand, since the probes do not require interaction, only a few hints about how to see the results they produce are given.

1. Zabbix Wrapper
=================

The Zabbix Wrapper provides a REST API as a way to facilitate interactions with the Zabbix server. The Zabbix server just provides a RCP interface as a way to interact remotely. Therefore, as a way to facilitate interaction in a more standardized way, Indigo provides this REST API, quite focused on the kind of operations that will be necessary at the PaaS layer.

1.1. Zabbix Server Configuration
--------------------------------
List of Zabbix Server the wrapper is pointing at:
```
GET: http://{url}/monitoring/adapters/{zabbix}/types
```

1.2. Groups and Hosts
---------------------
List of groups per each specified server: the servers can be:
* Infrastructure
* Service
* watcher
```
GET: http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups
```

List of hosts per specified group
```
GET: http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts
```

Update of group name
```
POST: http://{url}/monitoring/adapters/zabbix/types/{serverType*}/groups/{groupName}/
{
"newHostGroupName":"newGroupNameTobeSet"
}
```

Delete the specified group
```
DELETE: http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}
```

Host Creation
```
PUT:http://localhost:8082/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostName}
{
"ip":"182.25.25.25",
"uuid":"<uuid_openstack>",
"serviceCategory":"appaas",
"serviceId":"serviceId",
"atomicServices": ["apache"],
"activeMode": true
}
```

Delete Host
```
DELETE:http://localhost:8082/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostname}
```

Hosts information based on associated service id belonging to service
```
GET:http://localhost:8082/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts?serviceid={822}
```

Information on shot triggers and associated to host’s metrics
```
GET:http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts?thresholds=true
```

Information about specified host by its name
```
GET:http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostname}
```

Enable o disable specified host by its name
```
POST:http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostName}?update=disable
```

1.3. Metrics
------------
Enable or disable a specified metric by its name
```
POST:http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostName}/metrics/{metricName}?update=enable
```
Information about a specific metric by its name
```
GET: http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostName}/metrics/{metricName}
```

History of specified metric without time filter
```
GET:http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostName}/metrics/{metricName}/history
```

History of specified metric with time filter
```
POST:http://{url}/monitoring/adapters/{zabbix}/types/{serverType*}/groups/{groupName}/hosts/{hostName}/metrics/{metricsName}/history
{
"dateFrom":{
"year":"2015",
"month":"06",
"day":"10",
"time":{
"hh":"00",
"mm":"00",
"ss":"00"
}
},
"dateTo":{
"year":"2015",
"month":"06",
"day":"12",
"time":{
"hh":"13",
"mm":"13",
"ss":"00"
},
"upToNow":false
}
}
```

2. Zabbix Probes
================

2.1 Running the Probes
----------------------
In case the installation instructions were followed correctly, the provided Zabbix probes (OCCI and Heapster) should be running in the background periodically. In such case, they will be gathering information that, later on, is sent to the Zabbix Server. 

If this is not the case, it is possible to run manually the probes by using the following commands:
```
java -jar /usr/share/java/zabbix/occi-zabbix-probe-0.95-jar-with-dependencies.jar
java -jar /usr/share/java/zabbix/heapster-zabbix-probe-0.95-jar-with-dependencies.jar
```

These commands will run just once the monitoring probes, feeding the Zabbix Server with updated information, but be aware that those values will not be updated, unless they are programmed as Cron jobs.

2.2 Accessing Metrics
---------------------

The metrics generated can be accessed from the Zabbix GUI. Alternatively, they can be accessed through the Zabbix Wrapper, by sending the corresponding requests, as already described in the previous section.

The best option for looking at newest metrics is to click on the 'Monitoring' tab of the GUI and, once the sub-menu is shown, click on the 'Latest data' option. That will show a filter that can be used in order to search for the information we want. Since the defined templates are tagged with an Application field, the easiest way to find the metrics generated by the probes is:
* OCCI: Fill in the 'Application' field with the word OCCI and click on 'Filter'
* Heapster: Fill in the 'Application' field with the word Heapster and click on 'Filter'

This way, the last metrics will be listed in the GUI. Clicking on 'Graphs' (at the right of each item) it is possible to access historical information.