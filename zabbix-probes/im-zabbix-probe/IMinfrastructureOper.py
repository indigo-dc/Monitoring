import time
import json
import base64
from loadconfigs import LoadIMZabbixConfig
import requests
import logging

imconf = LoadIMZabbixConfig()
BASIC_RADL = imconf.IM_RADL
URL_BASE = imconf.IM_URLBASE
URL_REFRESH = imconf.AUTH_URLREFRESH

class ResponseIM:
	def __init__(self,statuscode, info):
		self.statuscode = statuscode
		self.info = info

def requestIM(method,url,data,headers,verify):

    if method == 'POST':
        try:
		r = requests.post(url, data=data, headers=headers, verify=verify)
		rq = ResponseIM(r.status_code,r.text)
        except requests.ConnectionError, e:
            logging.error( "* ConnectionError exception at method "+method+": " + str(e))
            rq = ResponseIM(111,e)

    elif method == 'GET':
        try:
            r = requests.get(url, data=data, headers=headers, verify=verify)
            rq = ResponseIM(r.status_code,r.text)
        except requests.ConnectionError, e:
            logging.error( "* ConnectionError exception at method "+method+": " + str(e))
            rq = ResponseIM(111,e)
        except requests.exceptions.InvalidHeader as e:
            logging.error( "* InvalidHeader exception: ---> " + str(e) + " <--")
            rq = ResponseIM(111,e)

    elif method == 'PUT':
        try:
            r = requests.put(url, data=data, headers=headers, verify=verify)
            rq = ResponseIM(r.status_code,r.text)
        except requests.ConnectionError, e:
            logging.error( "* ConnectionError exception at method "+method+": " + str(e))
            rq = ResponseIM(111,e)
        except requests.exceptions.InvalidHeader as e:
            logging.error( "* InvalidHeader exception: ---> " + str(e) + " <--")
            rq = ResponseIM(111,e)

    elif method == 'DELETE':
        try:
            r = requests.delete(url, data=data, headers=headers, verify=verify)
            rq = ResponseIM(r.status_code,r.text)
        except requests.ConnectionError, e:
            logging.error( "* ConnectionError exception at method "+method+": " + str(e))
            rq = ResponseIM(111,e)
        except requests.exceptions.InvalidHeader as e:
            logging.error( "* InvalidHeader exception: ---> " + str(e) + " <--")
            rq = ResponseIM(111,e)

    return rq


def list_infrastructure(IMHEADERS):

	r = requestIM('GET', URL_BASE, {}, IMHEADERS, False)

	if r.statuscode==200:
		ret = ResponseIM(r.statuscode,'list method OK')
	elif r.statuscode==111:
		ret = ResponseIM(r.statuscode,r.info)
	else:
		try:
			json_data = json.loads(r.info)
			ret = ResponseIM(r.statuscode,json_data['message'])
		except ValueError,e:
			ret = ResponseIM(111,e)
			logging.error("list_infrastructure"+str(e))

	return ret


def create_infrastructure(IMHEADERS):
	# hold on a little bit for the IM to get ready
	time.sleep(3)
	r = requestIM('POST',URL_BASE, BASIC_RADL, IMHEADERS, False)

	if r.statuscode==200:
		try:
			json_data = json.loads(r.info)
			ret = ResponseIM(r.statuscode,json_data['uri'])
		except ValueError,e:
			ret = ResponseIM(111,e)
			logging.error(e)

	elif r.statuscode==111:
		ret = ResponseIM(r.statuscode,r.info)
	else:
		try:
			json_data = json.loads(r.info)
			ret = ResponseIM(r.statuscode,json_data['message'])
		except ValueError,e:
			ret = ResponseIM(111,e)
			logging.error("create_infrastructure"+str(e))
	return ret


def start_infrastructure(IMHEADERS,uri_inf_id):

	time.sleep(3)
	r = requestIM('PUT', uri_inf_id+'/start',{} , IMHEADERS, False)

	if r.statuscode==200:
		ret = ResponseIM(r.statuscode,'start method OK')
	elif r.statuscode==111:

		ret = ResponseIM(r.statuscode,r.info)
	else:
		try:
			json_data = json.loads(r.info)
			ret = ResponseIM(r.statuscode,json_data['message'])
		except ValueError,e:
			ret = ResponseIM(111,e)
			logging.error("start_infrastructure"+str(e))

	return ret


def create_vm(IMHEADERS,uri_inf_id):

	time.sleep(3)
	r = requestIM('POST', uri_inf_id, BASIC_RADL, IMHEADERS, False)

	if r.statuscode == 200:
		ret = ResponseIM(r.statuscode,'Creation of VM is OK')
	elif r.statuscode==111:
		ret = ResponseIM(r.statuscode,r.info)
	else:
		try:
			json_data = json.loads(r.info)
			ret = ResponseIM(r.statuscode,json_data['message'])
		except ValueError,e:
			ret = ResponseIM(111,e)
			logging.error("create_vm"+str(e))

	return ret


def delete_infrastructure(IMHEADERS,uri_inf_id):

	time.sleep(3)
	r = requestIM('DELETE', uri_inf_id, {}, IMHEADERS, False)

	if r.statuscode == 200:
		ret = ResponseIM(r.statuscode,'delete_infrastructure is OK')
	elif r.statuscode==111:
		ret = ResponseIM(r.statuscode,r.info)
	else:
		try:
			json_data = json.loads(r.info)
			ret = ResponseIM(r.statuscode,json_data['message'])
		except ValueError,e:
			ret = ResponseIM(111,e)
			logging.error("delete_infrastructure"+str(e))

	return ret
