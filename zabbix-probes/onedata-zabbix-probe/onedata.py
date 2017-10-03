import requests
import json
import time
import socket
import sys
import base64

from loadconfigs import LoadOnedataZabbixConfig,Datacodejson

import logging
from logging.handlers import RotatingFileHandler

from requests.packages.urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)
from requests_oauthlib import OAuth2Session, TokenUpdated

BASE_PATH_PROVIDER ='/api/v3/oneprovider'
BASE_PATH_ZONE ='/api/v3/onezone'
BASE_PATH_PANEL ='/api/v3/onepanel'

# ---- log config ------------------------------------------------------
def log_setup(loglevel):
	log_handler = RotatingFileHandler(
		'./log/onedata-zabbix-agent.log',
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

# ----------------


def get_newtoken(client_id,urlref,token,refresh_header):
    logging.warning("getting newtoken")

    try:
        session = OAuth2Session(
        client_id,
        token=token,
        auto_refresh_url=urlref,
        auto_refresh_kwargs=refresh_header)

        r = session.get(urlref)

    except TokenUpdated as e:
        logging.info("TokenUpdated exception")
        return e

def update_tokenjson(access_token,ref_token):

    json_token = {
        'access_token': access_token,
        'token_type': 'Bearer',
        'refresh_token': ref_token,
        'expires_in': '-30'
    }

    return json_token

def update_MCRheader(tokenstring):

	prefix = 'indigo:'
	mcr = prefix + tokenstring
	UPD_HEADERS = {"X-Auth-Token" : mcr }
	return UPD_HEADERS


def request_onedata(url,urlheader,rqextra):
	try:
		r = requests.get(url, headers=urlheader,  verify=False)
		logging.debug("Response Code: %s", str(r.status_code))
		logging.debug("URL: %s", str(url))
	except requests.ConnectionError, e:
		logging.error( " ConnectionError exception when connecting to Onedata: " + str(e))
		ret = Datacodejson(111,e)
		return ret

	if not len(r.text):
		logging.warning("Received empty response from "+url)
		ret = Datacodejson('0',{})
	else:
		try:
			r_json = json.loads(r.text)
		except ValueError:
			logging.error("Unable to parse json. Url: "+url)
			ret = Datacodejson(111,'Unable to parse json')
			return ret

		if r.status_code == 200:
			ret = Datacodejson(r.status_code,r_json)
			return ret
		elif r.status_code == 401:
			# request new token
			logging.error("Error 401 ===> "+str(r_json["error"]))
			ret = Datacodejson(r.status_code,r_json)
		elif r.status_code == 403:
			logging.error("Error 403 ===> "+str(r_json["error"])+ " Description:"+str(r_json["error_description"]))
		else:
			logging.error("Error code "+ str(r.status_code))
		ret = Datacodejson(r.status_code,r_json)
	return ret

def build_url(zone,path):
	if zone == 'oneprovider':
		basepath = BASE_PATH_PROVIDER
		basehost = HOST_PROVIDER
		baseport = PORT_PROVIDER
	elif zone == 'onezone':
		basepath = BASE_PATH_ZONE
		basehost = HOST_ZONE
		baseport = PORT_ZONE
	else:
		logging.error( zone + " is not valid as base path.")
		return '-1'

	url = 'https://'+basehost+':'+str(baseport)+basepath+'/'+path
	logging.debug(url)
	return url

def get_list_of_spaces(mcr,glextra):
	urlreq = build_url('onezone','spaces')

	data = request_onedata(urlreq,mcr,glextra)
	
	spaceslist = []

	if data.statuscode == 200:
		jsondata = data.jsondata
		if int(len(jsondata)) < 1:
			return spaceslist
		for spc in jsondata["spaces"]:
			spaceslist.append(spc)
		return spaceslist
	elif data.statuscode == 401:
		spaceslist.append(401)
		spaceslist.append(data.jsondata)
		return spaceslist
	else:
		# No spaces or maybe user does not have list_spaces privilege.
		# Getting user's spaces...
		# GET /user/spaces
		# Returns the list of users' spaces.
		logging.info('No spaces... Trying another way to find spaces...')
		urlreq = build_url('onezone','user/spaces')
		time.sleep(3)
		data = request_onedata(urlreq,mcr,glextra)

		if data.statuscode == 200:
			jsondata = data.jsondata
			for spc in jsondata["spaces"]:
				spaceslist.append(spc)
			return spaceslist
		else:
			logging.warning('No spaces or maybe user does not have list_spaces privilege.')
			return None

def get_oz_spaces_spaceid(eachspace,urlheader,ozextra):
	#/spaces/space1
	urlreq = build_url('onezone','spaces/'+eachspace)
	data = request_onedata(urlreq,urlheader,ozextra)

	if data.statuscode == 200:
		data_json = data.jsondata
		if int(len(data_json)) < 1:
			return
		itemname = 'onezone.spaces.'+eachspace[:8]+'.generalinfo'
		itemData[itemname] = str(json.dumps(data.jsondata))
		#value_type 4 - text.
		itemValueType[itemname] = 4
		return itemData[itemname]


def get_group_info(eachspace,eachgroup,urlheader,ozextra):
	urlreq = build_url('onezone','spaces/'+eachspace+'/groups/'+eachgroup)
	sub_datagroup = request_onedata(urlreq,urlheader,ozextra)
	#return Datacodejson(sub_datagroup.statuscode,sub_datagroup)
	return sub_datagroup

def get_privileges_by_group(eachspace,eachgroup,urlheader,ozextra):
	urlreq = build_url('onezone','spaces/'+eachspace[:8]+'/groups/'+eachgroup+'/privileges')
	sub_datagroup = request_onedata(urlreq,urlheader,ozextra)
	#return Datacodejson(sub_datagroup.statuscode,sub_datagroup)
	return sub_datagroup

def get_oz_groups_by_space(eachspace,urlheader,ozextra):
	#/spaces/space1/groups
	urlreq = build_url('onezone','spaces/'+eachspace+'/groups')
	data = request_onedata(urlreq,urlheader,ozextra)

	if data.statuscode == 200:
		datagroups = data.jsondata["groups"]
		if int(len(datagroups)) < 1:
			return
		for eachgroup in datagroups:
			#/spaces/space1/groups/group1
			sub_datagroup = get_group_info(eachspace,eachgroup,urlheader,ozextra)
			#urlreq = build_url('onezone','spaces/'+eachspace+'/groups/'+eachgroup)
			#sub_datagroup = request_onedata(urlreq,urlheader,ozextra)

			if sub_datagroup.statuscode == 200:
				#type
				itemname = 'onezone.spaces.'+eachspace[:8]+'.groups.'+eachgroup+'.type'
				itemData[itemname] = str(sub_datagroup.jsondata['type'])
				#value_type 4 - text.
				itemValueType[itemname] = 4
				#groupid
				itemname = 'onezone.spaces.'+eachspace[:8]+'.groups.'+eachgroup+'.groupid'
				itemData[itemname] = str(sub_datagroup.jsondata['groupId'])
				itemValueType[itemname] = 4

			#/spaces/space1/groups/group1/privileges
			#urlreq = build_url('onezone','spaces/'+eachspace[:8]+'/groups/'+eachgroup+'/privileges')
			#sub_datagroup = request_onedata(urlreq,urlheader,ozextra)
			sub_datagroup = get_privileges_by_group(eachspace,eachgroup,urlheader,ozextra)

			if sub_datagroup.statuscode == 200:
				itemname = 'onezone.spaces.'+eachspace[:8]+'.groups.'+eachgroup+'.privileges'
				joinedData = ' '
				#value_type 4 - text.
				itemValueType[itemname] = 4
				itemData[itemname] = joinedData.join(sub_datagroup.jsondata['privileges'])
		return datagroups


def get_spaceprovider_info(eachspace,eachprov,urlheader,ozextra):
	urlreq = build_url('onezone','spaces/'+eachspace+'/providers/'+eachprov)
	sub_dataprov = request_onedata(urlreq,urlheader,ozextra)
	return sub_dataprov


def get_oz_providers_by_space(eachspace,urlheader,ozextra):
	#/spaces/space1/providers
	urlreq = build_url('onezone','spaces/'+eachspace+'/providers')
	data = request_onedata(urlreq,urlheader,ozextra)

	if data.statuscode == 200:
		dataprov = data.jsondata["providers"]
		if int(len(dataprov)) < 1:
			return
		joinedData = ' '
		itemname = 'onezone.spaces.'+eachspace[:8]+'.providers'
		itemData[itemname] = joinedData.join(data.jsondata['providers'])
		itemValueType[itemname] = 4

		for eachprov in dataprov:
			#/spaces/space1/providers/p1
			#urlreq = build_url('onezone','spaces/'+eachspace+'/providers/'+eachprov)
			#sub_dataprov = request_onedata(urlreq,urlheader,ozextra)
			sub_dataprov = get_spaceprovider_info(eachspace,eachprov,urlheader,ozextra)

			if sub_dataprov.statuscode == 200:
				providkeys = sub_dataprov.jsondata.keys()
				for provkey in providkeys:
					itemname = 'onezone.spaces.'+eachspace[:8]+'.providers.'+eachprov+'.'+provkey

					if provkey == 'urls':
						joinedData = ' '
						itemData[itemname] = str(joinedData.join(sub_dataprov.jsondata['urls']))
						itemValueType[itemname] = 4
					elif provkey == 'longitude':
						itemValueType[itemname] = 4
						itemData[itemname] = str(sub_dataprov.jsondata[provkey])
					elif provkey == 'latitude':
						itemValueType[itemname] = 4
						itemData[itemname] = str(sub_dataprov.jsondata[provkey])
					elif provkey == 'redirectionPoint':
						itemValueType[itemname] = 4
						itemData[itemname] = str(sub_dataprov.jsondata[provkey])
					elif provkey == 'providerId':
						itemValueType[itemname] = 4
						itemData[itemname] = str(sub_dataprov.jsondata[provkey])
					elif provkey == 'clientName':
						itemValueType[itemname] = 4
						itemData[itemname] = str(sub_dataprov.jsondata[provkey])
		return itemData

def get_space_user_info(eachspace,eachuser,urlheader,ozextra):
	urlreq = build_url('onezone','spaces/'+eachspace+'/users/'+eachuser)
	sub_datauser = request_onedata(urlreq,urlheader,ozextra)
	#return Datacodejson(sub_datauser.statuscode,sub_datauser)
	return sub_datauser


def get_oz_users_by_space(eachspace,urlheader,ozextra):
	#/spaces/space1/users
	urlreq = build_url('onezone','spaces/'+eachspace+'/users')
	data = request_onedata(urlreq,urlheader,ozextra)

	if data.statuscode == 200:
		data_json = data.jsondata
		if int(len(data_json)) < 1:
			return
		joinedData = ' '
		itemname = 'onezone.spaces.'+eachspace[:8]+'.users'
		itemData[itemname] = joinedData.join(data.jsondata['users'])
		#value_type 4 - text.
		itemValueType[itemname] = 4
		datauser = data.jsondata["users"]

		for eachuser in datauser:
			#/spaces/space1/users/user1
			#urlreq = build_url('onezone','spaces/'+eachspace+'/users/'+eachuser)
			#sub_datauser = request_onedata(urlreq,urlheader,ozextra)
			sub_datauser = get_space_user_info(eachspace,eachuser,urlheader,ozextra)
			itemname = 'onezone.spaces.'+eachspace[:8]+'.users.'+eachuser[:8]+".generalinfo"
			itemData[itemname] = str(json.dumps(sub_datauser.jsondata))
			#value_type 4 - text.
			itemValueType[itemname] = 4
			if sub_datauser.statuscode == 200:
				userkeys = sub_datauser.jsondata.keys()
				for userkey in userkeys:
					if len(sub_datauser.jsondata[userkey])>0:
						itemname = 'onezone.spaces.'+eachspace[:8]+'.users.'+eachuser[:8]+'.'+userkey
						#value_type 4 - text.
						itemValueType[itemname] = 4
						# set values to each key
						if userkey == 'connectedAccounts':
							itemValueType[itemname] = 0
							itemData[itemname] = len (sub_datauser.jsondata[userkey])

			#/spaces/space1/users/user3/privileges
			#urlreq = build_url('onezone','spaces/'+eachspace+'/users/'+eachuser+'/privileges')
			#sub_datauser = request_onedata(urlreq,urlheader,ozextra)
			sub_datauser = get_privileges_by_space_user(eachspace,eachuser,urlheader,ozextra)

			if sub_datauser.statuscode == 200:
				joinedData = ' '
				itemname = 'onezone.spaces.'+eachspace[:8]+'.users.'+eachuser[:8]+'.privileges'
				itemData[itemname] = joinedData.join(sub_datauser.jsondata['privileges'])
				#value_type 4 - text.
				itemValueType[itemname] = 4
		return itemData

def get_privileges_by_space_user(eachspace,eachuser,urlheader,ozextra):
	urlreq = build_url('onezone','spaces/'+eachspace+'/users/'+eachuser+'/privileges')
	sub_datauser = request_onedata(urlreq,urlheader,ozextra)
	return sub_datauser


# metrics by oneprovider
# ----------------------------------------------------------------------------------

def get_op_storage_quota_by_space(eachspace,urlheader,opextra):
	#https://172.17.0.8:8443/api/v3/oneprovider/metrics/space/space1?metric=storage_quota
	urlreq = build_url('oneprovider','metrics/space/'+eachspace+'?metric=storage_quota')
	data = request_onedata(urlreq,urlheader,opextra)
	#print "Response code (storage_quota):" + str(data.statuscode)+"  Data ---> " + str(data.jsondata)

	if data.statuscode == 200:
		data_json = data.jsondata
		if len(data_json) == 0:
			logging.info("get_op_storage_quota_by_space::empty data for "+str(urlreq))
			return

		l=len(data_json[0]['rrd']['data'])
		for i in range(l):
			indx = l - i -1
			if str(data_json[0]['rrd']['data'][indx][0]) != 'None':
				itemname = 'oneprovider.space.'+eachspace+'.storage_quota'
				itemData[itemname] = str(data_json[0]['rrd']['data'][indx][0])
				#value_type 0 - numeric float;
				itemValueType[itemname] = 0
				return itemData[itemname]

def get_op_storage_used_by_space(eachspace,urlheader,opextra):
	#https://172.17.0.8:8443/api/v3/oneprovider/metrics/space/space1?metric=storage_used
	urlreq = build_url('oneprovider','metrics/space/'+eachspace+'?metric=storage_used')
	data = request_onedata(urlreq,urlheader,opextra)
	#print "Response code (storage_used):" + str(data.statuscode)+"  Data ---> " + str(data.jsondata)

	if data.statuscode == 200:
		data_json = data.jsondata
		if len(data_json) == 0:
			logging.info("get_op_storage_used_by_space::empty data for "+str(urlreq))
			return

		try:
			l=len(data_json[0]['rrd']['data'])
		except IndexError as ie:
			l=0
			logging.warning("get_op_storage_used_by_space::"+str(ie))

		if l>0:
			for i in range(l):
				indx = l - i -1
				if str(data_json[0]['rrd']['data'][indx][0]) != 'None':
					itemname = 'oneprovider.space.'+eachspace+'.storage_used'
					itemData[itemname] = str(data_json[0]['rrd']['data'][indx][0])
					#value_type 0 - numeric float;
					itemValueType[itemname] = 0
					return itemData[itemname]

def get_op_data_access_by_space(eachspace,urlheader,opextra):
	#https://172.17.0.8:8443/api/v3/oneprovider/metrics/space/space1?metric=data_access
	urlreq = build_url('oneprovider','metrics/space/'+eachspace+'?metric=data_access')
	data = request_onedata(urlreq,urlheader,opextra)
	#print "Response code (data_access):" + str(data.statuscode)+"  Data ---> " + str(data.jsondata)

	if data.statuscode == 200:
		data_json = data.jsondata
		if len(data_json) == 0:
			logging.info("get_op_data_access_by_space::empty data for "+str(urlreq))
			return

		try:
			l=len(data_json[0]['rrd']['data'])
		except IndexError as ie:
			l=0
			logging.info("get_op_data_access_by_space::"+str(ie))

		if l>0:
			for i in range(l):
				indx = l - i -1
				#check data_access_write
				if str(data_json[0]['rrd']['data'][indx][1]) != 'None':
					#check data_access_read
					if str(data_json[0]['rrd']['data'][indx][0]) != 'None':
						ret = {}
						#set write
						itemname = 'oneprovider.space.'+eachspace+'.data_access_write'
						itemData[itemname] = str(data_json[0]['rrd']['data'][indx][1])
						ret['data_access_write'] =  itemData[itemname]
						#value_type 0 - numeric float;
						itemValueType[itemname] = 0
						#set read
						itemname = 'oneprovider.space.'+eachspace+'.data_access_read'
						itemData[itemname] = str(data_json[0]['rrd']['data'][indx][0])
						ret['data_access_read'] =  itemData[itemname]
						#value_type 0 - numeric float;
						itemValueType[itemname] = 0
						return ret

def get_op_block_access_by_space(eachspace,urlheader,opextra):
	#https://172.17.0.8:8443/api/v3/oneprovider/metrics/space/space1?metric=block_access
	urlreq = build_url('oneprovider','metrics/space/'+eachspace+'?metric=block_access')
	data = request_onedata(urlreq,urlheader,opextra)
	#print "Response code (block_access):" + str(data.statuscode)+"  Data ---> " + str(data.jsondata)
	if data.statuscode == 200:
		data_json = data.jsondata
		if len(data_json) == 0:
			logging.info("get_op_block_access_by_space::empty data for "+str(urlreq))
			return
		try:
			l=len(data_json[0]['rrd']['data'])
		except IndexError as ie:
			l=0
			logging.warning("get_op_block_access_by_space::"+str(ie))

		if l>0:
			for i in range(l):
				indx = l - i -1
				#check data_access_write
				if str(data_json[0]['rrd']['data'][indx][1]) != 'None':
					#check data_access_read
					if str(data_json[0]['rrd']['data'][indx][0]) != 'None':
						#set write
						ret = {}
						itemname = 'oneprovider.space.'+eachspace+'.block_access_write'
						itemData[itemname] = str(data_json[0]['rrd']['data'][indx][1])
						ret['block_access_write'] =  itemData[itemname]
						#value_type 0 - numeric float;
						itemValueType[itemname] = 0
						#set read
						itemname = 'oneprovider.space.'+eachspace+'.block_access_read'
						itemData[itemname] = str(data_json[0]['rrd']['data'][indx][0])
						ret['block_access_read'] =  itemData[itemname]
						#value_type 0 - numeric float;
						itemValueType[itemname] = 0
						#break
						return ret

		else:
			#print "No data Received for block_access"
			pass


def get_op_connected_users_by_space(eachspace,urlheader,opextra):
	#https://172.17.0.8:8443/api/v3/oneprovider/metrics/space/space1?metric=connected_users
	urlreq = build_url('oneprovider','metrics/space/'+eachspace+'?metric=connected_users')
	data = request_onedata(urlreq,urlheader,opextra)
	#print "Response code:" + str(data.statuscode)+"  Data ---> " + str(data.jsondata)
	if data.statuscode == 200:
		data_json = data.jsondata
		if len(data_json) == 0:
			logging.info("get_op_connected_users_by_space::empty data for "+str(urlreq))
			return
		try:
			l=len(data_json[0]['rrd']['data'])
		except IndexError as ie:
			l=0
			logging.warning("get_op_connected_users_by_space::"+str(ie))

		if l>0:
			for i in range(l):
				indx = l - i -1
				if str(data_json[0]['rrd']['data'][indx][0]) != 'None':
					itemname = 'oneprovider.space.'+eachspace+'.connected_users'
					itemData[itemname] = str(data_json[0]['rrd']['data'][indx][0])
					#value_type 3 - numeric unsigned;
					itemValueType[itemname] = 3
					break
			return itemData[itemname]

def get_onedata_itemdata_by_space(each_space,urlheader,goid_extra):

	#onenzone items groups
	global itemData
	itemData = {}
	if odconf.onezone_spaces_namespace == '1':
		get_oz_spaces_spaceid(each_space,urlheader,goid_extra)
	if odconf.onezone_spaces_namespace_groups == '1':
		get_oz_groups_by_space(each_space,urlheader,goid_extra)
	if odconf.onezone_spaces_namespace_providers == '1':
		get_oz_providers_by_space(each_space,urlheader,goid_extra)
	if odconf.onezone_spaces_namespace_users == '1':
		get_oz_users_by_space(each_space,urlheader,goid_extra)

	#oneprovider items groups
	if odconf.oneprovider_space_namespace_storage_quota == '1':
		get_op_storage_quota_by_space(each_space,urlheader,goid_extra)
	if odconf.oneprovider_space_namespace_storage_used == '1':
		get_op_storage_used_by_space(each_space,urlheader,goid_extra)
	if odconf.oneprovider_space_namespace_data_access == '1':
		get_op_data_access_by_space(each_space,urlheader,goid_extra)
	if odconf.oneprovider_space_namespace_block_access == '1':
		get_op_block_access_by_space(each_space,urlheader,goid_extra)
	if odconf.oneprovider_space_namespace_connected_users == '1':
		get_op_connected_users_by_space(each_space,urlheader,goid_extra)

	return itemData

# ---------------------------------------------------------

odconf = LoadOnedataZabbixConfig()
URL_REFRESH = odconf.AUTH_URLREFRESH
HOST_PROVIDER = odconf.HOST_PROVIDER
HOST_ZONE = odconf.HOST_ZONE
PORT_ZONE = odconf.PORTONEZONE
PORT_PROVIDER = odconf.PORTONEPROVIDER
itemData = {}
itemValueType = {}
