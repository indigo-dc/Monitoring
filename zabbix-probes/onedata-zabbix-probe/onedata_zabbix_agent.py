import json
import socket
import sys
import base64
import ConfigParser
import argparse
import zabbix
import onedata
import time

from loadconfigs import Datacodejson
from loadconfigs import LoadOnedataZabbixConfig

import requests
from requests.exceptions import ConnectionError
from requests.packages.urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)

import logging
from onedata import log_setup

def initializing():

	# load zabbix config
	global zconf
	zconf = LoadOnedataZabbixConfig()
	global ZABBIX_TEMPLATE
	ZABBIX_TEMPLATE = zconf.ZABBIX_TEMPLATE
	global AGENT_DELAY
	AGENT_DELAY = int(zconf.AGENT_DELAY)
	LOGLEVEL = zconf.LOGLEVEL
	log_setup(LOGLEVEL)

	try:
		print zconf.error
		logging.error(zconf.error)
	except AttributeError:
		pass

	hi = "onedata-zabbix agent says hello!"
	print hi
	logging.info(hi)

	# Parse input arguments
	parser = argparse.ArgumentParser(description='Monitorize IM operations.')

	parser.add_argument('-i','--client_id', help='ID Client credential', required=True)
	parser.add_argument('-s','--client_secret', help='SECRET Client credential', required=True)
	parser.add_argument('-r','--token_refresh', help='STRING of refresh token')
	parser.add_argument('-t','--token', help='STRING of access token')

	parser.add_argument('-u','--zabbix_user',     help='Zabbix user credential', required=True)
	parser.add_argument('-p','--zabbix_password', help='Zabbix password credential', required=True)
	parser.add_argument('-a','--zabbix_url',      help='Zabbix URL API', required=True)
	parser.add_argument('-v','--zabbix_server',   help='Zabbiz server IP', required=True)

	args = parser.parse_args()

	# IAM - OENDATA token stuff
	if args.token == None:
		print "Use -t or --token option to pass a valid token."
		sys.exit()
	elif args.token_refresh == None:
		print "Use -r or --token_refresh option to pass a valid refresh token."
		sys.exit()

	global CLIENT_ID
	global CLIENT_SECRET
	global MACAROON_HEADER
	global JSON_REQ_TOKEN
	global URL_REFRESH
	global extra
	global ZABBIX_TOKEN
	global json_zabbix_credentials

	CLIENT_ID = args.client_id
	CLIENT_SECRET = args.client_secret
	MACAROON_HEADER = onedata.update_MCRheader(args.token)
	JSON_REQ_TOKEN = onedata.update_tokenjson(args.token,args.token_refresh)
	URL_REFRESH = zconf.AUTH_URLREFRESH
	extra = { 'client_id': CLIENT_ID, 'client_secret': CLIENT_SECRET}

	# Zabbix token stuff
	json_zabbix_credentials = {}
	json_zabbix_credentials["zabbix_user"] = args.zabbix_user
	json_zabbix_credentials["zabbix_password"] = args.zabbix_password
	json_zabbix_credentials["zabbix_url"] = args.zabbix_url
	json_zabbix_credentials["zabbix_server"] = args.zabbix_server

	# login to zabbix
	ZABBIX_TOKEN = zabbix.getZbxAuthenToken(json_zabbix_credentials)


def main():
	global MACAROON_HEADER
	global JSON_REQ_TOKEN
	global extra

	if ZABBIX_TOKEN is not None:

		# obtain list of items stored into zabbix
		listItemsZabbix = []
		listItemsZabbix = zabbix.get_zabbix_itemlist(ZABBIX_TOKEN,ZABBIX_TEMPLATE,json_zabbix_credentials)
		templateid = zabbix.getTemplateId(ZABBIX_TOKEN,ZABBIX_TEMPLATE,json_zabbix_credentials)

		# obtain list of aplications stored into zabbix
		app_list = []
		app_list = zabbix.getAppList(ZABBIX_TOKEN,templateid,json_zabbix_credentials)

		if len(app_list) == 0:
			logging.warning(" Could NOT find any Application at zabbix. Creating ONEZONE and ONEPROVIDER groups...")
			zabbix.applicationCreate(ZABBIX_TOKEN,'ONEZONE',templateid,json_zabbix_credentials)
			zabbix.applicationCreate(ZABBIX_TOKEN,'ONEPROVIDER',templateid,json_zabbix_credentials)
			app_list = zabbix.getAppList(ZABBIX_TOKEN,templateid,json_zabbix_credentials)
			logging.warning(app_list)

		# get list of onedata spaces
		spac_list = []
		spac_list = onedata.get_list_of_spaces(MACAROON_HEADER,extra)

		if spac_list[0] == 401:
			if 'token' in spac_list[1]["error"]:
				if 'not valid' in spac_list[1]["error"]:
					spac_list = None
					logging.info("Need token refresh")
					clid = extra["client_id"]
					newtoken = onedata.get_newtoken(clid,URL_REFRESH,JSON_REQ_TOKEN,extra)
					NEW_ACCESS_TOKEN = newtoken.token['access_token']
					MACAROON_HEADER = onedata.update_MCRheader(NEW_ACCESS_TOKEN)
					JSON_REQ_TOKEN = onedata.update_tokenjson(NEW_ACCESS_TOKEN,JSON_REQ_TOKEN['refresh_token'])

		if spac_list is not None:
			# gather data from onedata
			for each_space in spac_list:
			    Data = onedata.get_onedata_itemdata_by_space(each_space,MACAROON_HEADER,extra)
			    for singleitem in Data:
			        valtype = int(onedata.itemValueType[singleitem])
			        try:
			            itemid = listItemsZabbix[singleitem]
			        except KeyError:
						# Type of the item: 2 (Zabbix trapper)
						paramtype = 2
						logging.info(" must add " + singleitem + " of type " + str(valtype) + " to zabbix")
						hid = zabbix.createItem(ZABBIX_TOKEN,singleitem,singleitem,templateid,paramtype,valtype,json_zabbix_credentials)

						if hid is not None:
							itemparts = singleitem.split(".")
							apnam = itemparts[0]
							apnamUC = apnam.upper()
							applicationid = app_list[apnamUC]
							if len(applicationid)>0:
								zabbix.addItemApplication(ZABBIX_TOKEN,applicationid,hid,json_zabbix_credentials)
							listItemsZabbix[singleitem] = hid
							singledata = Data[singleitem]
							zabbix.send_data(singleitem,singledata,json_zabbix_credentials)
			        else:
			            singledata = Data[singleitem]
			            zabbix.send_data(singleitem,singledata,json_zabbix_credentials)
		else:
			logging.warning(" Could not get a list of spaces.")

# ----- RUN -----------------------------------------------------------------

if __name__ == '__main__':
	initializing()
	while True:
		main()
		time.sleep(AGENT_DELAY)
print"agent says bye!"
