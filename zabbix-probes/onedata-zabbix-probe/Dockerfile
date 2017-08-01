############################################################
# Dockerfile to build ONEDATA-zabbix monitor agent images
# Based on Centos
# docker build -t indigodatacloud/your_im_zabbix_image_name /path/to/the/docker/folder
############################################################

# Set base image
FROM centos
USER root

# ---- Install python requirements ----
# Add repo
RUN rpm -iUvh https://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-9.noarch.rpm

# Install pip
RUN yum -y install python-pip

# Install required modules
RUN pip install mock
RUN pip install requests
RUN pip install requests_oauthlib

# Install git and clone
RUN yum install git -y
RUN git clone https://github.com/indigo-dc/Monitoring.git

# Prepare workdir and log dir
WORKDIR /Monitoring/zabbix-probes/onedata-zabbix-probe
RUN mkdir log; cd log; touch od-zbx-agent.log
RUN chmod -R 777 /Monitoring/zabbix-probes/onedata-zabbix-probe

# copy agent packd if you want to work from local package
# ADD ./onedatazabbix /opt/onedata_zabbix_agent
# WORKDIR /opt/onedata_zabbix_agent


# Set the default command to execute when creating a new container
CMD python onedata_zabbix_agent.py -u=Admin -p=zabbix -t=$ONEDATA_TOKEN -i $CLIENT_ID -s $CLIENT_SECRET -r $REFRESH -a $ZABBIX_URL -v $ZABBIX_SERVER
