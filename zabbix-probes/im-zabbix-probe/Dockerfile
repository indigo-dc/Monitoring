############################################################
# Dockerfile to build onedata-zabbix monitor agent images
# Based on Centos
# [root@indigo-dc-new eoortiz]# docker build -t eoortiz/im_zbx_built .
############################################################

# Set base image 
FROM centos

# ---- Install python requirements ----

# Add repo
RUN rpm -iUvh https://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-9.noarch.rpm

# Install pip
RUN yum -y install python-pip

# Install required modules
RUN pip install mock
RUN pip install requests
RUN pip install requests_oauthlib

# copy agent pack
ADD ./imzabbix /opt/im_zabbix_agent

WORKDIR /opt/im_zabbix_agent

# ENVIRONMENT VARIABLES
#ENV CLIENT_ID=$CLIENT_ID CLIENT_SECRET=$CLIENT_SECRET JSON_TOKEN_FILE=$JSON_TOKEN_FILE


# Set the default command to execute when creating a new container

#CMD python probeim.py -i $CLIENT_ID -s $CLIENT_SECRET -f $JSON_TOKEN_FILE
CMD python probeim.py -i $CLIENT_ID -s $CLIENT_SECRET -t $TOKEN -r $REFRESH

