############################################################
# Dockerfile to build onedata-zabbix monitor agent images
# Based on Centos
############################################################

# Set base image 
FROM centos

# ---- Install python requirements ----

# Add repo
RUN rpm -iUvh http://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-8.noarch.rpm

# Install pip
RUN yum -y install python-pip

# Install required modules
RUN pip install mock
RUN pip install requests

# copy agent pack
ADD ./od_zbx /opt/onedata_zabbix_agent

WORKDIR /opt/onedata_zabbix_agent

# Set the default command to execute when creating a new container
# Run agent (mock)
CMD python test_odzbx_agent.py


