# 1 Main Features

This probe works by getting the list of available providers and, for each Openstack API available, a VM is created, inspected and deleted, as a way to confirm that the main operations to be done are working as expected in the provider under evaluation. The probe is able to monitor several providers concurrently and it sends all the gathered metrics to the Zabbix server collecting all the information.

# 2 Pre-Requisites

Not fulfilling these requisites will have a negative impact in the execution of the probes.

 * It requires a CMDB available, so it will be possible to retrieve the list of available providers. Having no access to a CMDB means that the probe will not be able to retrieve a list of providers to monitor, therefore not doing anything;
 * Since the implementation of the probe is in Java, it requires, Java8 JVM to be already installed.

# 3 Installation

## 3.1 Ubuntu 16.04
  1. You have to enable the INDIGO - DataCloud packages repositories. See full instructions
  [here](https://indigo-dc.gitbooks.io/indigo-datacloud-releases/content/generic_installation_and_configuration_guide_2.html#id4).
  2. After that you need to install the Java 8 Runtime Environment with
  ```
  # sudo apt install openjdk-8-jre-headless
  ```
  3. Finally you can install the probe with the command
  ```
  # sudo apt install openstack-zabbix-probe
  ```

## 3.2 Centos 7
  1. You have to enable the INDIGO - DataCloud packages repositories. See full instructions
  [here](https://indigo-dc.gitbooks.io/indigo-datacloud-releases/content/generic_installation_and_configuration_guide_2.html#id4).
  2. After that you need to install the Java 8 Runtime Environment with
  ```
  # sudo yum install java-1.8.0-openjdk-headless
  ```
  3. Finally you can install the probe with the command
  ```
  # sudo yum install openstack-zabbix-probe
  ```

## 3.3 Cron Job
Although the probes can be run just on demand, the best option is to configure them as Cron jobs. That can be configured by editing the configuration file with the following command:
```
# crontab -e
```

Then, add the following lines:
```
0 * * * * java -jar /usr/share/java/zabbix/openstack-zabbix-probe-1.2-jar-with-dependencies.jar
```

This means that the probe will run every hour at xx:00. Modify this configuration according to your needs, so the probes will run when required.

# 4 Configuration

The Openstack probe requires to modify the openstackprobe.properties (inserted in /etc/zabbix/ for linux systems in C:\zabbixconfig\ for Windows ones) file in order to set the following parameters:
* java.keystore - Set here the full location of the security certificates keystore
* cmdb.location - Provide the full URL of the CMDB component, providing the information about the available providers
* wait.real.vm.creation - Provides the user to get information about the real creation response time of a machine or just the immediate response from the client
* is-iam-authenticated - If set to true the probe tries authenticating itself to Openstack via IAM. in this case just make sure to have filled all the fields in the property file especially the following ones:
* iam.location - Endpoint of IAM
* iam.username
* iam.password
* iam.clientid
* iam.clientsecret
* openstack.project - Name of the tenant where deploying the VMs
* providers.exceptions - If set to true, providers in the oszones.yml file will have a custom configuration instead of the standard one specified in openstackprobe.properties. Please, see next section for how to configure this custom providers.
* iam.protocol - Stands for the method used to get the unscoped token from openstack by using the already obtained bearer token
* iam.identity.provider - The Identity Provider to pass to OpenStack to get the Keystone token.

The probe now supports IAM authenticatiom so that with SSO it can communicate with all the Opestack supporting IAM authentication standard.
However, if skipping Iam authentication from porperty file, just make sure to insert the credentials of openstack tenant (where the probe tries creating, deleting and inspecting the instances) inside oszones.yml file located at the same path as openstackprobe.properties.

# 5 Per provider configuration
Due to differences in the configuration of providers, we need to know which protocol, provider and project to use when getting the Keystone token. A standard one can be configured in openstackprobe.properties by filling the openstack.project, iam.protocol and iam.identity.provider properties. However, if any provider deviates from that, its custom configuration can be specified in an extra file named oszones.yml that resides in the same folder as openstackprobe.properties file. The format of such file is:
```yaml
cloud-providers-zones:
  - name: <provider_name>
    password: "<pswd>"
    username: "<usr>"
    tenant: <project>
```

Where:
* provider_name is the name of the provider as it appears in the CMDB
* usr is the user configured to access keystone (not needed if using IAM)
* pswd is the password to access keystone (not needed if using IAM)
* tenant is the project in which the test VMs will be created to monitor the Open Stack installation. This project has to have enough permissions to create, get and delete VMs for the configured user (either IAM user or the custom one if not using IAM)

# 5 Potential Issues

In the case of providers requiring some communication using SSL, if the provider certificate is not signed by a known entity, the JVM may throw exceptions. In such case, it is necessary to register the corresponding certificate with the following command:
```
# keytool -importcert -trustcacerts -alias trustycert -file trustycert.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts"
```
