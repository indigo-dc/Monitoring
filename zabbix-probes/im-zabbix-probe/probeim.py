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
from loadconfigs import LoadIMZabbixConfig


#global
monitorItems = {}

def log_setup(loglevel):
	log_handler = RotatingFileHandler(
		'./log/probeim.log',
		maxBytes=1048576,
		backupCount=5)

	formatter = logging.Formatter('%(asctime)s - %(levelname)s: %(message)s -- %(filename)s::%(funcName)s line 	%(lineno)d','%b %d %H:%M:%S')
	formatter.converter = time.gmtime
	log_handler.setFormatter(formatter)
	logger = logging.getLogger()
	logger.addHandler(log_handler)

	if loglevel == 'ERROR':
		lvl = 40
	elif loglevel == 'WARNING':
		lvl = 30
	elif loglevel == 'INFO':
		lvl = 20
	elif loglevel == 'DEBUG':
		lvl = 10
	else:
		lvl = 30

	logger.setLevel(lvl)


def main(TOKEN,IMHEADERS):

	# local
	monitorItemsDict = {}

	global GLOBAL_ACCESS_TOKEN
	global GLOBAL_TOKEN
	global GLOBAL_IMHEADERS

	# CREATE INFRASTRUCTURE
	ci = IMinfrastructureOper.create_infrastructure(IMHEADERS)
	url_infr = ci.info

	if ci.statuscode == 401:
		# refresh token
		monitorItemsDict['imstatus']= '0 - '+str(ci.info)
		logging.info("Need token refresh")
		newtoken = tokenmng.get_newtoken(CLIENT_ID,IMinfrastructureOper.URL_REFRESH,TOKEN,extra)
		GLOBAL_ACCESS_TOKEN = newtoken.token['access_token']
		#print "GLOBAL_ACCESS_TOKEN = " +GLOBAL_ACCESS_TOKEN
		GLOBAL_TOKEN = tokenmng.update_tokenjson(GLOBAL_ACCESS_TOKEN,REFRESH_TOKEN)
		GLOBAL_IMHEADERS = tokenmng.update_imheaders(GLOBAL_ACCESS_TOKEN)

	elif ci.statuscode == 111:
	    logging.error("Could NOT start CREATION INFRASTRUCTURE process: " +str(ci.info))
	    monitorItemsDict['imstatus']= '0 - '+ str(ci.info)
	    return monitorItemsDict

	elif ci.statuscode == 200:
	    # START INFRASTRUCTUREWARNING
	    si = IMinfrastructureOper.start_infrastructure(IMHEADERS,url_infr)

	    if si.statuscode == 200:

	        time.sleep(1)
	        # LIST INFRASTRUCTURE
	        li = IMinfrastructureOper.list_infrastructure(IMHEADERS)

	        if li.statuscode == 200:
	            # CREATE VM
	            cv = IMinfrastructureOper.create_vm(IMHEADERS,url_infr)

	            if cv.statuscode == 200:
	                # DELETE INFRASTRUCTURE
	                di = IMinfrastructureOper.delete_infrastructure(IMHEADERS,url_infr)

	                if di.statuscode == 200:
						monitorItemsDict['imstatus'] = 1
						logging.info("All operations have been completed successfully.")
	                else:
	                    logging.error("Infrastructure could NOT be DELETED")
	                    monitorItemsDict['imstatus']= '0 - '+ str(di.info)
	            else:
	                logging.error("VM could NOT be CREATED")
	                monitorItemsDict['imstatus']= '0 - '+ str(cv.info)
	        else:
	            logging.error( "Infrastructure could NOT be LISTED")
	            monitorItemsDict['imstatus']= '0 - '+str(li.info)
	    else:
	        logging.error("Infrastructure could NOT be STARTED")
	        monitorItemsDict['imstatus']= '0 - '+str(si.info)
	else:
		logging.error("Infrastructure could NOT be CREATED")
		monitorItemsDict['imstatus']= '0 - '+ str(ci.info)

	return monitorItemsDict


# ----- RUN -----------------------------------------------------------------

if __name__ == '__main__':

	# load conf
	zconf = LoadIMZabbixConfig()
	global AGENT_DELAY
	AGENT_DELAY = int(zconf.AGENT_DELAY)

	global LOGLEVEL
	LOGLEVEL = zconf.LOGLEVEL
	log_setup(LOGLEVEL)

	logging.info("Initializing --------------")

	# Parse input arguments
	parser = argparse.ArgumentParser(description='Monitorize IM operations.')
	parser.add_argument('-i','--client_id',     help='ID Client credential',        required=True)
	parser.add_argument('-s','--client_secret', help='SECRET Client credential',    required=True)

	parser.add_argument('-u','--zabbix_user',     help='Zabbix user credential', required=True)
	parser.add_argument('-p','--zabbix_password', help='Zabbix password credential', required=True)
	parser.add_argument('-a','--zabbix_url',      help='Zabbix URL API', required=True)
	parser.add_argument('-v','--zabbix_server',   help='Zabbiz server IP', required=True)

	parser.add_argument('-f','--token_file',    help='Filename of JSON token file')
	parser.add_argument('-t','--token',         help='STRING of access token')
	parser.add_argument('-r','--token_refresh', help='STRING of refresh token')
	args = parser.parse_args()

	# get client credential and token
	logging.info("Initializing --------------")
	json_globals = tokenmng.getClientTokenInfoFromArgs(args)

	json_zabbix_credentials = {}
	json_zabbix_credentials["zabbix_user"] = args.zabbix_user
	json_zabbix_credentials["zabbix_password"] = args.zabbix_password
	json_zabbix_credentials["zabbix_url"] = args.zabbix_url
	json_zabbix_credentials["zabbix_server"] = args.zabbix_server

	CLIENT_ID = json_globals["CLIENT_ID"]
	CLIENT_SECRET = json_globals["CLIENT_SECRET"]
	GLOBAL_ACCESS_TOKEN = json_globals["GLOBAL_ACCESS_TOKEN"]
	REFRESH_TOKEN = json_globals["REFRESH_TOKEN"]

	extra = {
	    'client_id': CLIENT_ID,
	    'client_secret': CLIENT_SECRET,
	}

	GLOBAL_IMHEADERS = tokenmng.update_imheaders(GLOBAL_ACCESS_TOKEN)
	GLOBAL_TOKEN = tokenmng.update_tokenjson(GLOBAL_ACCESS_TOKEN,REFRESH_TOKEN)

	stepin = True;

	while stepin:
		monitorItems = {}
		monitorItems = main(GLOBAL_TOKEN,GLOBAL_IMHEADERS)
		zabbix.send_data_zabbix(monitorItems,json_zabbix_credentials)
		time.sleep(AGENT_DELAY)

print "bye!"
