import time
import socket
import base64
import json
import argparse
import sys

import zabbix
import IMinfrastructureOper
import tokenmng

import requests
from requests.exceptions import ConnectionError

import logging
from logging.handlers import RotatingFileHandler

from loadconfigs import Datacodejson
from loadconfigs import LoadZabbixConfig
from loadconfigs import LoadIMConfig

#global
monitorItems = {}

def log_setup():
	log_handler = RotatingFileHandler(
		'./log/probeim.log',
		maxBytes=1048576,
		backupCount=5)
	formatter = logging.Formatter('%(asctime)s - %(levelname)s: %(message)s','%b %d %H:%M:%S')
	formatter.converter = time.gmtime
	log_handler.setFormatter(formatter)
	logger = logging.getLogger()
	logger.addHandler(log_handler)
	logger.setLevel(logging.WARNING)
	#logger.setLevel(logging.INFO)


def main(TOKEN,IMHEADERS):

	# load conf
	zconf = LoadZabbixConfig()
	global AGENT_DELAY
	AGENT_DELAY = int(zconf.AGENT_DELAY)

	global GLOBAL_ACCESS_TOKEN
	global GLOBAL_TOKEN
	global GLOBAL_IMHEADERS

	# local
	monitorItemsDict = {}

	monitorItemsDict['create_inf']= 0
	monitorItemsDict['start_inf']= 0
	monitorItemsDict['create_vm']= 0
	monitorItemsDict['delete_inf']= 0
	monitorItemsDict['list_inf']= 0
	monitorItemsDict['num_error']= 0
	monitorItemsDict['msg_error']= 0

	# CREATE INFRASTRUCTURE
	ci = IMinfrastructureOper.create_infrastructure(IMHEADERS)
	url_infr = ci.info

	if ci.statuscode == 401:
	    # refresh token
	    monitorItemsDict['num_error']= ci.statuscode
	    monitorItemsDict['msg_error']= str(ci.info)
	    logging.info("Need token refresh")
	    newtoken = tokenmng.get_newtoken(CLIENT_ID,IMinfrastructureOper.URL_REFRESH,TOKEN,extra)
	    GLOBAL_ACCESS_TOKEN = newtoken.token['access_token']
	    GLOBAL_TOKEN = tokenmng.update_tokenjson(GLOBAL_ACCESS_TOKEN,REFRESH_TOKEN)
	    GLOBAL_IMHEADERS = tokenmng.update_imheaders(GLOBAL_ACCESS_TOKEN)

	elif ci.statuscode == 111:
	    logging.error(str(ci.info))
	    monitorItemsDict['num_error']= ci.statuscode
	    monitorItemsDict['msg_error']= str(ci.info)
	    return monitorItemsDict

	elif ci.statuscode == 200:
	    monitorItemsDict['create_inf'] = 1
	    # START INFRASTRUCTURE
	    si = IMinfrastructureOper.start_infrastructure(IMHEADERS,url_infr)

	    if si.statuscode == 200:

	        monitorItemsDict['start_inf'] = 1
	        time.sleep(1)
	        # LIST INFRASTRUCTURE
	        li = IMinfrastructureOper.list_infrastructure(IMHEADERS)

	        if li.statuscode == 200:
	            monitorItemsDict['list_inf'] = 1
	            # CREATE VM
	            cv = IMinfrastructureOper.create_vm(IMHEADERS,url_infr)

	            if cv.statuscode == 200:

	                monitorItemsDict['create_vm'] = 1
	                # DELETE INFRASTRUCTURE
	                di = IMinfrastructureOper.delete_infrastructure(IMHEADERS,url_infr)

	                if di.statuscode == 200:
	                    monitorItemsDict['delete_inf'] = 1
	                    logging.info("Todo el proceso se ha completado satisfactoriamente")
	                else:
	                    logging.error("No se pudo borrar infraestructura")
	                    monitorItemsDict['num_error']= di.statuscode
	                    monitorItemsDict['msg_error']= str(di.info)
	            else:
	                logging.error("No se pudo crear VM")
	                monitorItemsDict['num_error']= cv.statuscode
	                monitorItemsDict['msg_error']= str(cv.info)
	        else:
	            logging.error( "No se pudo listar infraestructura")
	            monitorItemsDict['num_error']= li.statuscode
	            monitorItemsDict['msg_error']= str(li.info)
	    else:
	        logging.error("No se pudo iniciar infraestructura")
	        monitorItemsDict['num_error']= si.statuscode
	        monitorItemsDict['msg_error']= str(cs.info)
	else:
	    logging.error("no se pudo crear infraestructura")

	#print "end main"
	return monitorItemsDict


# ----- RUN -----------------------------------------------------------------

if __name__ == '__main__':

	log_setup()

	# Parse input arguments
	parser = argparse.ArgumentParser(description='Monitorize IM operations.')
	parser.add_argument('-i','--client_id',     help='ID Client credential',        required=True)
	parser.add_argument('-s','--client_secret', help='SECRET Client credential',    required=True)
	parser.add_argument('-f','--token_file',    help='Filename of JSON token file')
	parser.add_argument('-t','--token',         help='STRING of access token')
	parser.add_argument('-r','--token_refresh', help='STRING of refresh token')
	args = parser.parse_args()

	# get client credential and token
	logging.info("Initializing --------------")
	json_globals = tokenmng.getClientTokenInfoFromArgs(args)

	CLIENT_ID = json_globals["CLIENT_ID"]
	CLIENT_SECRET = json_globals["CLIENT_SECRET"]
	GLOBAL_ACCESS_TOKEN = json_globals["GLOBAL_ACCESS_TOKEN"]
	REFRESH_TOKEN = json_globals["REFRESH_TOKEN"]

	# print"CLIENT_ID: ",CLIENT_ID
	# print"CLIENT_SECRET: ",CLIENT_SECRET
	# print"GLOBAL_ACCESS_TOKEN: ",GLOBAL_ACCESS_TOKEN
	# print"REFRESH_TOKEN: ",REFRESH_TOKEN

	extra = {
	    'client_id': CLIENT_ID,
	    'client_secret': CLIENT_SECRET,
	}

	GLOBAL_IMHEADERS = tokenmng.update_imheaders(GLOBAL_ACCESS_TOKEN)
	GLOBAL_TOKEN = tokenmng.update_tokenjson(GLOBAL_ACCESS_TOKEN,REFRESH_TOKEN)

	stepin = True;

	while stepin:
	    monitorItems = main(GLOBAL_TOKEN,GLOBAL_IMHEADERS)
	    zabbix.send_data_zabbix(monitorItems)
		time.sleep(AGENT_DELAY)

print "bye!"
