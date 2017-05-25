############################################################
# Dockerfile to build IM-zabbix monitor agent images
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
WORKDIR /Monitoring/zabbix-probes/im-zabbix-probe
RUN mkdir log; cd log; touch probeim.log
RUN chmod -R 777 /Monitoring/zabbix-probes/im-zabbix-probe

# Set the default command to execute when creating a new container
CMD python probeim.py -i $CLIENT_ID -s $CLIENT_SECRET -t $TOKEN -r $REFRESH -u $ZABBIX_USER -p $ZABBIX_PASSWORD -v $ZABBIX_SERVER -a $ZABBIX_API_URL
