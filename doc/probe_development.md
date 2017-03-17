# Probe development guide

Any probe should gather metrics from a set of hosts and send them to a central zabbix server. As this is a straightforward process, some utility classes have been created in the zabbix-probes-common project

## Configuration 

All probes should have some form of configuration, for example, under which credentials they are operating, what's the system path to some certificates or what's the URL to an authorization system. To easen this task, the class `PropertiesManager` will take care of:

- Loading a configuration file that will reside in C:\\zabbix folder for Windows hosts and /etc/zabbix folder for Linux ones. Calling `PropertiesManager.loadProperties(fileName)` method will try to read properties from either `C:\\zabbix\fileName` or `/etc/zabbix/fileName`
- Loading a configuration file that will be somewhere else. To do so, use the overloaded method `PropertiesManager.loadProperties(Reader confFile)` that will accept a reader pointing to any stream

This method should be called just once upon initialization of the probe and after that, properties can be read with the operation `PropertiesManager.getProperty(String property)`, `PropertiesManager.getProperty(String property, String defaultValue)` or `PropertiesManager.getListProperty(String property)` for properties that accept a list as a value.
Talking about lists, this class will use [Apache Commons Configuration](https://commons.apache.org/proper/commons-configuration/index.html) so it will accept any format configuration file as defined [here.](https://commons.apache.org/proper/commons-configuration/userguide/howto_properties.html)

As the behavior of the zabbix probes has some components in common, some properties are expected to be provided in every configuration file:
- zabbix.ip: IP to the central zabbix server
- zabbix.port: This property is optional. If not present, the data will be sent to ${zabbix.ip}:10051, which is the default zabbix port
- zabbix.wrapper.location: A URL with the location of the Zabbix Wrapper that the probe will use to register hosts dynamically.

Apart from that, every probe is free to define as much properties as they are needed.

## Metrics collection

The next step after reading the configuration is getting useful metrics from some host. To do so, there has to be an implementation of the class `MetricsCollector`. Please, notice that this is a very simple interface with just one method:
* `ZabbixMetrics getMetrics()`: This method should communicate with the host to monitor, gather the required metrics and then return them.

`ZabbixMetrics` class should contain the metrics gathered from a single host. As such it has the following properties to fill:
* `String hostName`: Name of the host in which the metrics were gathered. If the host name is not already registered in the server, it will be automatically registered under this name
* `Map<String, String> metrics`: A key-value map with the metrics
* `long timestamp`: The timestamp in which the metrics were gathered

## Sending metrics

Metrics collected by a collector will be sent to the configured central server by the class `ZabbixClient`. This class will do all the work by itself by:
- Checking that the host is registered through the `ZabbixWrapperClient` class
- If not, register it
- Once registered, send the data present in `ZabbixMetrics` by using [Java bindings](https://github.com/hengyunabc/zabbix-sender) so there is no need to install the zabbix agent on the same machine in which the probe is operating.
- Upon completion, it will return the result of the operation
  - State of the operation: if it has been successful or not
  - Number of processed elements
  - Number of failed elements
  
I'm sorry to not give much more information but Zabbix doesn't return anything else.

# Running it all

Zabbix probes are designed to be standalone Java applications running as a cron job. As such, for simple cases, `ProbeThread` class is offered to help in the development of new probes. This class is generic and it should be parametrized with a class that extends `MetricsCollection`. Extending this abstract class will force the implementation of the method
- `T createCollector()`: Where T should implement MetricsCollector. This method is basically a way to initialize the collector, which will do the metrics collection job as its name implies. Note that your implementation may need some initialization upon creation so this is the place to do it.
- `ProbeThread(String category, String group, String template)`: Notice that this class doesn't have a default empty constructor. That will force you to call the protected default constructor by passing the probe category, group and template under which it will operate. This properties will be used to register hosts dynamically with the Zabbix Wrapper. For more information about this, please see the [Zabbix Wrapper user guide](user.md)

Your class should contain a main method and this method should call `SenderResult run(String propertiesFile)` method that will read your configuration file, call the collector which will get the metrics and then send them to the central service.


# A special case: Lifecycle management

Some probes will get metrics of a host by doing a series of actions that will spawn something in the host, then check that it's working and then destroying this test object. That object can be a container, a job, a virtual machine, etc. To help in the development of this situation, we offer the class `LifecycleCollector`. This class depend on the existence of a template with the following properties:

- `<op>.status`: A boolean with the success status of the operation
- `<op>.result`: With a code result of the operation. Usually this will be an HTTP result returned by the monitored host but it can be given any other meaning.
- `<op>.responseTime`: With the time it took to execute the operation

To all this, `<op>` will be an operation of:
- `clear`: This one is executed at the beginning of the cycle to clear any possible remaining of a previously run job
- `create`: This operation will be responsible of creating the object
- `run`: This operation is responsible of verifying that the object is being run
- `delete`: This operation is responsible of deleting the object to finish its lifecycle

The operations will be run in sequential order and they depend one on another, so an unsuccessful create, for example, will mean that run and delete will not be run and will be returned as failed automatically.

All this is achieved by extending the `LifecycleCollector` class which will force the implementation of the above mentioned operations. Aditionally it will force the implementation of the method `String getHostName()` which should return the name of the host which is being monitored.

For some examples of how to implement this, please have a look at the classes `ChronosCollector` and `MarathonCollector` in the _mesos-zabbix-probe_ project which implement collectors that manage the lifecycle of jobs and containers.

# REST clients

Much of the communication with external systems will be done by calling REST interfaces. To easily create REST clients, we can use [Feign](https://github.com/OpenFeign/feign). This framework uses annotated interfaces to easily create REST clients with minimal boilerplate code. For concrete examples, you can have a look at the [documentation](https://github.com/OpenFeign/feign) and for concrete examples you can have a look at the `MesosClient` class in _mesos-zabbix-probe_ project of `ZabbixWrapperClient` in the _zabbix-probes-common_ project.
As a helper to create simple clients that communicate with JSON objects, the class `ProbeClientFactory` provides a `T getClient(Class<T> clientClass, String baseUrl)` method that, given an annotated interface and a base URL, will return a fully functional REST client.
