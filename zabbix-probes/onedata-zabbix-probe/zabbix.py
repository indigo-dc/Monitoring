import requests
import json
import logging
import socket
import sys
import base64
from onedata import log_setup

from requests.exceptions import ConnectionError
from requests.packages.urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)

try:
    from ConfigParser import ConfigParser
except ImportError:
    # Python 3
    from configparser import ConfigParser

from loadconfigs import LoadOnedataZabbixConfig

zconf = LoadOnedataZabbixConfig()

def request_zabbix(json_request):

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
                logging.error("Unable to parse json:" +str(respost.text))
                return None
            #there is a response
            return json_respost
    except ConnectionError as e:
        logging.error( " ConnectionError exception when connecting to Zabbix: " + str(e))
        #print str(e)
        return None


def send_data(item,datanostr):
    # print "item: "+str(item)
    # print "data: "+str(datanostr)
    # print "--------------------"
    data = str(datanostr)
    if len(data) < 1:
        print "NO HAY DATOS PARA "+str(item)
        return None
    if len(item) < 1:
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
        # print >>sys.stderr, 'enviando "%s"' % message
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
            #print "getItemsList:: "+ str(key)+" => "+str(itemList[key])
        return itemList
    else:
        return None


def getAppList(token,templid):
    appslist_get_json = {
        "jsonrpc": "2.0",
        "method": "application.get",
        "params": {
            "hostids": templid,
            "output": ["name","applicationid","templateids","hostid"]
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(appslist_get_json)

    if json_respost is not None:
        #there is a response
        appList = {}
        l = len(json_respost['result'])
        for i in range(l):
            appname = str(json_respost['result'][i]['name'])
            appList[appname] =  str(json_respost['result'][i]['applicationid'])
        return appList
    else:
        return None


def applicationCreate(token, appname, hostid):
    app_create_json = {
        "jsonrpc": "2.0",
        "method": "application.create",
        "params": {
            "name": appname,
            "hostid": hostid
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(app_create_json)

    if json_respost is not None:
        resappid = json_respost['result']['applicationids'][0]
        logging.info( "applicationCreate::result= "+str(resappid))
        return resappid
    else:
        logging.error(  "applicationCreate::result= None!!! ")
        return None


def addItemApplication(token,applicationid,itemid):
    additemtoapp_json = {
        "jsonrpc": "2.0",
        "method": "application.massadd",
        "params": {
            "applications": [ { "applicationid": applicationid } ],
            "items": [ { "itemid": itemid } ]
        },
        "auth": token,
        "id": 1
    }

    json_respost = request_zabbix(additemtoapp_json)

    if json_respost is not None:
        aid = json_respost['result']['applicationids'][0]
        logging.info("addItemApplication::result= "+str(aid))
        return aid
    else:
        logging.error(  "addItemApplication::result= None!!!")
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

    if json_respost is not None:
        tid = json_respost['result'][0]['templateid']
        logging.info( "getTemplateId::result= "+str(tid))
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
            #    print "get_zabbix_itemlist:: item: " + eachitem + " = " + itemsInZabbix[eachitem]
        else:
            logging.error( "No template option.")
            return None
    else:
        logging.error( "No token.")
        return None
    return itemsInZabbix
