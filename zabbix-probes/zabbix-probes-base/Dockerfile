FROM alpine:latest
MAINTAINER Jose Antonio Sanchez <jose.sanchezm@atos.net>

# Install java and zabbix agent
RUN apk --no-cache --update add openjdk8-jre-base zabbix-agent
RUN mkdir /usr/share/java
RUN mkdir /usr/share/java/zabbix
ONBUILD COPY *.jar /usr/share/java/zabbix
ONBUILD COPY src/main/resources/*.properties /etc/zabbix