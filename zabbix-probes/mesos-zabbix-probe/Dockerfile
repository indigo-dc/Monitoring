FROM alpine:latest
MAINTAINER Jose Antonio Sanchez <jose.sanchezm@atos.net>

# Install java and zabbix agent
RUN apk --no-cache --update add openjdk8-jre-base nss
RUN mkdir /usr/share/java
RUN mkdir /usr/share/java/zabbix
COPY mesos-zabbix-probe-1.3-jar-with-dependencies.jar /usr/share/java/zabbix/mesos-zabbix-probe.jar
COPY src/mesosprobe.sh /usr/bin/mesosprobe.sh
RUN chmod +x /usr/bin/mesosprobe.sh
ENTRYPOINT ["mesosprobe.sh"]
CMD []