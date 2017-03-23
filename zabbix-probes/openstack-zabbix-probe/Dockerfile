FROM ubuntu:14.04

USER root
#Install Java
RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:git-core/ppa -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

# Define working directory.
WORKDIR /root

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Make sure config files directory exists
RUN mkdir -p /etc/zabbix/
ENV OS_PROBE_CONF /etc/zabbix/
VOLUME $OS_PROBE_CONF

#Install Maven and git
RUN apt-get update
RUN apt-get install maven -y
RUN apt-get install git -y

#GET the Application from repository
#RUN wget https://github.com/indigo-dc/Monitoring/blob/master/zabbix-probes/openstack-zabbix-probe/openstack-zabbix-probe-1.01-jar-with-dependencies.jar
RUN git clone https://github.com/indigo-dc/Monitoring.git

RUN cd /root/Monitoring/zabbix-probes \
    && mvn clean install; exit 0
RUN cp /root/Monitoring/zabbix-probes/openstack-zabbix-probe/openstack-zabbix-probe-1.01-jar-with-dependencies.jar /root

#Install zabbix agent and copy it on
#RUN wget http://repo.zabbix.com/zabbix/2.4/rhel/7/x86_64/zabbix-release-2.4-1.el7.noarch.rpm \
#	&& apt-get install -y zabbix-agent

#RUN chmod -R 777 /etc/zabbix/
#    && chmod -R 777 /var/log/zabbix/ \
#    && chmod -R 777 /var/run/zabbix/
#ENV ZABBIX_METADATA jbossas

COPY launch.sh /launch.sh
RUN chmod 777 /launch.sh

ENTRYPOINT sh /launch.sh
