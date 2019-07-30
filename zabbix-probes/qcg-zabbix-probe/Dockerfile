FROM alpine:latest
MAINTAINER Damian Kaliszan <damian.kaliszan@man.poznan.pl>

# Install java and zabbix agent
RUN apk --no-cache --update add openjdk8-jre-base nss
RUN mkdir /usr/share/java
RUN mkdir /usr/share/java/zabbix
COPY qcg-zabbix-probe-1.4-jar-with-dependencies.jar /usr/share/java/zabbix/qcg-zabbix-probe.jar
COPY src/qcgprobe.sh /usr/bin/qcgprobe.sh
RUN chmod +x /usr/bin/qcgprobe.sh
ENTRYPOINT ["qcgprobe.sh"]
CMD []