import json
import logging
try:
    from ConfigParser import ConfigParser
except ImportError:
    # Python 3
    from configparser import ConfigParser

def read_json_config(fileitemsjson):
    fullpathitemjson = "./conf/" + fileitemsjson
    with open(fullpathitemjson) as config_items_file:
		try:
			dataconfitems = json.loads(config_items_file)
			ret = Datacodejson('1',dataconfitems)
		except ValueError,e:
			logging.error("Could NOT load JSON "+fullpathitemjson+" Error: "+e )
			ret = Datacodejson('0',{})
    return ret

class Datacodejson:
	def __init__(self,statuscode, jsondata):
		self.statuscode = statuscode
		self.jsondata = jsondata


class LoadZabbixConfig(ConfigParser):
    def __init__(self):

        zabbixcfg = ConfigParser()
        zabbixcfg.read(["conf/zabbix.cfg"])

        if zabbixcfg.has_option("connection","uri"):
        	self.ZABBIX_URI = zabbixcfg.get("connection","uri")
        else:
        	logging.error( "There is no uri option into connection section.")

        if zabbixcfg.has_option("connection","server_add"):
        	self.ZABBIX_SERVER = zabbixcfg.get("connection","server_add")
        else:
        	logging.error( "There is no server_add option into connection section.")

        if zabbixcfg.has_option("connection","username"):
        	if zabbixcfg.has_option("connection","password"):
        		self.ZABBIX_USER = zabbixcfg.get("connection","username")
        		self.ZABBIX_PASSWORD = zabbixcfg.get("connection","password")
        	else:
        		logging.error("There is no password option into connection section.")
        else:
        	logging.error( "There is no username option into connection section.")

        if zabbixcfg.has_option("hosts","template"):
        	self.ZABBIX_TEMPLATE = zabbixcfg.get("hosts","template")
        else:
        	logging.error( "There is no template option into hosts section.")

        if zabbixcfg.has_option("hosts","monitoredhost"):
        	self.ZABBIX_MONITORED_HOST = zabbixcfg.get("hosts","monitoredhost")
        else:
        	logging.error( "There is no monitoredhost option into hosts section.")

        if zabbixcfg.has_option("connection","agent_delay"):
        	self.AGENT_DELAY = zabbixcfg.get("connection","agent_delay")
        else:
        	self.AGENT_DELAY = 300

        self.ZABBIX_HEADERS = {'content-type': 'application/json-rpc'}


class LoadIMConfig(ConfigParser):

    def __init__(self):
        imcfg = ConfigParser()
        imcfg.read(["conf/auth.cfg"])

        if imcfg.has_option("im","radl"):
        	self.IM_RADL = imcfg.get("im","radl")
        else:
        	logging.error( "There is no radl option into im section.")

        if imcfg.has_option("im","urlbase"):
        	self.IM_URLBASE = imcfg.get("im","urlbase")
        else:
        	logging.error( "There is no urlbase option into im section.")

        if imcfg.has_option("iam","urlrefresh"):
        	self.AUTH_URLREFRESH = imcfg.get("iam","urlrefresh")
        else:
        	logging.error( "There is no urlrefresh option into auth section.")
