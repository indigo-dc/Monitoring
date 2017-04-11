import requests
import json
import logging
import socket
import sys
import base64

from requests.exceptions import ConnectionError
from loadconfigs import LoadZabbixConfig

try:
    from ConfigParser import ConfigParser
except ImportError:
    # Python 3
    from configparser import ConfigParser

zconf = LoadZabbixConfig()
ZABBIX_USER = zconf.ZABBIX_USER
ZABBIX_PASSWORD = zconf.ZABBIX_PASSWORD
ZABBIX_URI = zconf.ZABBIX_URI
ZABBIX_TEMPLATE = zconf.ZABBIX_TEMPLATE
#AGENT_DELAY = int(zconf.AGENT_DELAY)

def request_zabbix(json_request):

    logging.info( "request_zabbix::method = "+str(json_request['method']))

    try:
        respost = requests.post(zconf.ZABBIX_URI, data=json.dumps(json_request), headers=zconf.ZABBIX_HEADERS)
        #print"Response Code:", str(respost.status_code)
    	#print"text: [%s]" %respost.text
        logging.info(str(respost.text))

        if not len(respost.text):
            logging.error("There is no answer for item.get request.")
            return None
        else:
            try:
                json_respost = json.loads(respost.text)
            except ValueError:
                logging.error("Unable to parse json (request_zabbix):" +str(respost.text))
                return None
            #there is a response
            return json_respost
    except ConnectionError as e:
        logging.error(e)
        #print str(e)
        return None


def send_data(item,data):
    #print "--- send_data -------" + str(data)
    if len(str(data))<1:
        return None
    if len(item)<1:
        return None
    # socket TCP/IP
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = (zconf.ZABBIX_SERVER, 10051)
    sock.connect(server_address)

    host = zconf.ZABBIX_MONITORED_HOST

    if len(host) < 1:
        logging.error( "No host caught from configuration file. Please check 'monitoredhost' option.")
        return None

    key = item

    try:
        message = '<req>\n<host>' + host.encode('base64','strict') + '</host>\n<key>' + key.encode('base64','strict') + '</key>\n<data>' + data.encode('base64','strict') + '</data>\n</req>\n'
        #print >>sys.stderr, 'enviando "%s"' % message
        sock.sendall(message)
        data = sock.recv(1024)
        #print >>sys.stderr, 'recibiendo "%s"' % data
    finally:
        sock.close()

    return data


def getZbxAuthenToken(username,passwd):
    authen_json={
        "jsonrpc": "2.0",
        "method": "user.login",
        "params": {
            "user": username,
            "password": passwd
        },
        "id": 1
    }

    json_respost = request_zabbix(authen_json)

    if json_respost is not None:
        result = json_respost['result']
        return result
    else:
        return None


def getItemsList(token,id):
    item_get_json={
        "jsonrpc": "2.0",
        "method": "item.get",
        "params": {
            "output": ["name","key_","itemid","value_type","hostid"],
            "hostids": id
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(item_get_json)

    if json_respost is not None:
        #there is a response
        itemList = {}
        l = len(json_respost['result'])
        for i in range(l):
            key = str(json_respost['result'][i]['name'])
            itemList[key] =  str(json_respost['result'][i]['itemid'])
            # print "getItemsList:: "+ str(key)+" => "+str(itemList[key])
        return itemList
    else:
        return None


def createTemplate(token,tname,groupid,hostidtolink):
    template_create_json = {
        "jsonrpc": "2.0",
        "method": "template.create",
        "params": {
            "host": tname,
            "groups": {"groupid": groupid},
            "hosts":[{"hostid": hostidtolink}]
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(template_create_json)

    if json_respost is not None:
        restemid = json_respost['result']['templateids'][0]
        #print "createTemplate ",restemid
        logging.info( "createTemplate::result= "+str(restemid))
        return restemid
    else:
        logging.error(  "createTemplate::result= None!!! ")
        return None


def createItem(token,itemname,key,hostid,paramtype,valtype):
    item_create_json = {
        "jsonrpc": "2.0",
        "method": "item.create",
        "params": {
            "name": itemname,
            "key_": key,
            "hostid": hostid,
            "type": paramtype,
            "value_type": valtype,
            "delay": 30
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(item_create_json)

    if json_respost is not None:
        resitemid = json_respost['result']['itemids'][0]
        logging.info( "createItem::result= "+str(resitemid))
        return resitemid
    else:
        logging.error(  "createItem::result= None!!! ")
        return None


def getHostId(token,hostname):
    gethost_json = {
        "jsonrpc": "2.0",
        "method": "host.get",
        "params": {
            "filter": { "host": [ hostname ] }
        },
        "auth": token,
        "id": 1
    }

    json_resp = request_zabbix(gethost_json)

    if json_resp is not None:
        hid = json_resp['result'][0]['hostid']
        logging.info( "getHostId::result= "+str(hid))
        return hid
    else:
        logging.error( "getHostId::result= None!!! ")
        return None


def getTemplateId(token,template_name):
    template_get_json={
        "jsonrpc": "2.0",
        "method": "template.get",
        "params": {
            "output": "templateid",
            "filter": {
                "host": [ template_name ]
            }
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(template_get_json)
    #print "getTemplateId for ---> "+template_name
    #print"json_respost = ",json_respost

    l = len(json_respost['result'])

    if l == 0:
        json_respost = None

    if json_respost is not None:
        tid = json_respost['result'][0]['templateid']
        #logging.info( "getTemplateId::result= "+str(tid))
        return tid
    else:
        logging.error( "getTemplateId::result= None!!! ")
        return None


def get_zabbix_itemlist(token,template):
    if token != '':
        if template != '':
            # get template ID
            templateid = getTemplateId(token,template)
            # get list of items from template
            itemsInZabbix = {}
            itemsInZabbix = getItemsList(token,templateid)
            #for eachitem in itemsInZabbix:
                #print "get_zabbix_itemlist:: item: " + eachitem + " = " + itemsInZabbix[eachitem]
        else:
            logging.error( "No template option.")
            return None
    else:
        logging.error( "No token.")
        return None
    return itemsInZabbix


def send_data_zabbix(monitorItemsDict):

    # login to zabbix
    ztoken = getZbxAuthenToken(ZABBIX_USER,ZABBIX_PASSWORD)
    if ztoken is not None:
        #print "Zabbix token seems good."
        valtype = 4
        # Type of the item: 2 (Zabbix trapper)
        paramtype = 2

        hostid = getHostId(ztoken,zconf.ZABBIX_MONITORED_HOST)

        # obtain list of items stored into zabbix
        templateid = getTemplateId(ztoken,ZABBIX_TEMPLATE)

        if templateid is None:
            #print "Hay que crear el template."
            templateid = createTemplate(ztoken,ZABBIX_TEMPLATE,1,hostid)

        listItemsZabbix = []
        listItemsZabbix = get_zabbix_itemlist(ztoken,ZABBIX_TEMPLATE)

        # send data
        for singleitem,singledata in monitorItemsDict.items():
            #print "--send--> ",singleitem,':',singledata
            try:
                itemid = listItemsZabbix[singleitem]
            except KeyError:
                logging.info(" must add " + singleitem + " of type " + str(valtype) + " to zabbix")
                hid = createItem(ztoken,singleitem,singleitem,templateid,paramtype,valtype)
                if hid is not None:
                    listItemsZabbix[singleitem] = hid
                    send_data(singleitem,str(singledata))
            else:
                send_data(singleitem,str(singledata))
    else:
        logging.error("There's no Zabbix token :(")
        print"There's no zabbix token :("
