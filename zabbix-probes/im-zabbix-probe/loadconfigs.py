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


class LoadIMZabbixConfig(ConfigParser):
    def __init__(self):

        imzabbixcfg = ConfigParser()
        imzabbixcfg.read(["conf/imzabbix.conf"])

        if imzabbixcfg.has_option("zabbix","template"):
        	self.ZABBIX_TEMPLATE = imzabbixcfg.get("zabbix","template")
        else:
        	logging.error( "There is no template option into zabbix section.")

        if imzabbixcfg.has_option("zabbix","monitoredhost"):
        	self.ZABBIX_MONITORED_HOST = imzabbixcfg.get("zabbix","monitoredhost")
        else:
        	logging.error( "There is no monitoredhost option into zabbix section.")

        if imzabbixcfg.has_option("zabbix","agent_delay"):
        	self.AGENT_DELAY = imzabbixcfg.get("zabbix","agent_delay")
        else:
        	self.AGENT_DELAY = 300

        if imzabbixcfg.has_option("im","radl"):
        	self.IM_RADL = imzabbixcfg.get("im","radl")
        else:
        	logging.error( "There is no radl option into im section.")

        if imzabbixcfg.has_option("im","urlbase"):
        	self.IM_URLBASE = imzabbixcfg.get("im","urlbase")
        else:
        	logging.error( "There is no urlbase option into im section.")

        if imzabbixcfg.has_option("iam","urlrefresh"):
        	self.AUTH_URLREFRESH = imzabbixcfg.get("iam","urlrefresh")
        else:
        	logging.error( "There is no urlrefresh option into iam section.")

        if imzabbixcfg.has_option("log","loglevel"):
        	self.LOGLEVEL = imzabbixcfg.get("log","loglevel")
        else:
        	logging.error( "There is no loglevel option into log section.")


        self.ZABBIX_HEADERS = {'content-type': 'application/json-rpc'}
