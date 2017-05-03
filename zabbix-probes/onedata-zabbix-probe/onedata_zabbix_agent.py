import json
import socket
import sys
import base64
import ConfigParser
import argparse
import  zabbix
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
	global ZABBIX_TEMPLATE
	global AGENT_DELAY
	global zconf

	# load zabbix config
	zconf = LoadOnedataZabbixConfig()
	ZABBIX_TEMPLATE = zconf.ZABBIX_TEMPLATE
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
	parser.add_argument('-t','--token', help='STRING of access token')
	parser.add_argument('-u','--user', help='STRING of zabbix username')
	parser.add_argument('-p','--passwd', help='STRING of zabbix password')
	args = parser.parse_args()

	global token

	if args.token == None:
		token = zconf.TOKEN
	else:
		token = args.token

	if args.user == None:
		try:
			ZABBIX_USER = zconf.ZABBIX_USER
		except AttributeError:
			print "You can use -u or --user option. Eg: -u=myuser or --user=myuser"
			sys.exit()
	else:
		ZABBIX_USER = args.user

	if args.passwd == None:
		try:
			ZABBIX_PASSWORD = zconf.ZABBIX_PASSWORD
		except AttributeError:
			print zconf.error
			print "You can use -p or --passwd option. Eg: -p=mypassword or --passwd=mypassword"
			sys.exit()
	else:
		ZABBIX_PASSWORD = args.passwd

	# login to zabbix
	token = zabbix.getZbxAuthenToken(ZABBIX_USER,ZABBIX_PASSWORD)


def main():



	if token is not None:

		# obtain list of items stored into zabbix
		listItemsZabbix = []
		listItemsZabbix = zabbix.get_zabbix_itemlist(token,ZABBIX_TEMPLATE)

		templateid = zabbix.getTemplateId(token,ZABBIX_TEMPLATE)

		# obtain list of aplications stored into zabbix
		app_list = []
		app_list = zabbix.getAppList(token,templateid)

		if len(app_list) == 0:
			logging.warning(" Could NOT find any Application at zabbix. Creating ONEZONE and ONEPROVIDER groups...")
			zabbix.applicationCreate(token,'ONEZONE',templateid)
			zabbix.applicationCreate(token,'ONEPROVIDER',templateid)
			app_list = zabbix.getAppList(token,templateid)
			logging.warning(app_list)

		# get list of onedata spaces
		spac_list = []
		spac_list = onedata.get_list_of_spaces()

		if spac_list is not None:
			# gather data from onedata
			for each_space in spac_list:
			    Data = onedata.get_onedata_itemdata_by_space(each_space)
			    for singleitem in Data:
			        valtype = int(onedata.itemValueType[singleitem])
			        try:
			            itemid = listItemsZabbix[singleitem]
			        except KeyError:
						# Type of the item: 2 (Zabbix trapper)
						paramtype = 2
						logging.info(" must add " + singleitem + " of type " + str(valtype) + " to zabbix")
						hid = zabbix.createItem(token,singleitem,singleitem,templateid,paramtype,valtype)

						if hid is not None:
							itemparts = singleitem.split(".")
							apnam = itemparts[0]
							apnamUC = apnam.upper()
							applicationid = app_list[apnamUC]
							if len(applicationid)>0:
								zabbix.addItemApplication(token,applicationid,hid)
							listItemsZabbix[singleitem] = hid
							singledata = Data[singleitem]
							zabbix.send_data(singleitem,singledata)
			        else:
			            singledata = Data[singleitem]
			            zabbix.send_data(singleitem,singledata)
		else:
			logging.warning(" Could not get a list of spaces.")

# ----- RUN -----------------------------------------------------------------

if __name__ == '__main__':

	initializing()

	while True:
		main()
		time.sleep(AGENT_DELAY)

print"agent says bye!"
