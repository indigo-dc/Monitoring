try:
    from ConfigParser import ConfigParser
except ImportError:
    # Python 3
    from configparser import ConfigParser


class Datacodejson:
	def __init__(self,statuscode, jsondata):
		self.statuscode = statuscode
		self.jsondata = jsondata


class LoadOnedataZabbixConfig(ConfigParser):
	def __init__(self):

		config = ConfigParser()
		config.read(["conf/onedata-zabbix.cfg"])

		if config.has_option("zabbix","template"):
			self.ZABBIX_TEMPLATE = config.get("zabbix","template")
		else:
			self.error = "There is no template option into zabbix section."

		if config.has_option("zabbix","monitoredhost"):
			self.ZABBIX_MONITORED_HOST = config.get("zabbix","monitoredhost")
		else:
			self.error = "There is no monitoredhost option into zabbix section."

		if config.has_option("zabbix","agent_delay"):
			self.AGENT_DELAY = config.get("zabbix","agent_delay")
		else:
			self.AGENT_DELAY = 300

		self.ZABBIX_HEADERS = {'content-type': 'application/json-rpc'}

		# onedata section

		if config.has_option("onedata","portZone"):
			self.PORTONEZONE = config.get("onedata","portZone")
		else:
			self.error = "There is no portZone option into onedata section."

		if config.has_option("onedata","portProvider"):
			self.PORTONEPROVIDER = config.get("onedata","portProvider")
		else:
			self.error = "There is no portProvider option into onedata section."

		if config.has_option("onedata","hostProvider"):
			self.HOST_PROVIDER = config.get("onedata","hostProvider")
		else:
			self.error = "There is no hostProvider option into onedata section."

		if config.has_option("onedata","hostZone"):
			self.HOST_ZONE = config.get("onedata","hostZone")
		else:
			self.error = "There is no hostZone option into onedata section."

		# items section

		if config.has_option("items","onezone_spaces_namespace"):
			self.onezone_spaces_namespace = config.get("items","onezone_spaces_namespace")

		if config.has_option("items","onezone_spaces_namespace_groups"):
			self.onezone_spaces_namespace_groups = config.get("items","onezone_spaces_namespace_groups")

		if config.has_option("items","onezone_spaces_namespace_providers"):
			self.onezone_spaces_namespace_providers = config.get("items","onezone_spaces_namespace_providers")

		if config.has_option("items","onezone_spaces_namespace_users"):
			self.onezone_spaces_namespace_users = config.get("items","onezone_spaces_namespace_users")

		if config.has_option("items","oneprovider_space_namespace_storage_quota"):
			self.oneprovider_space_namespace_storage_quota = config.get("items","oneprovider_space_namespace_storage_quota")

		if config.has_option("items","oneprovider_space_namespace_storage_used"):
			self.oneprovider_space_namespace_storage_used = config.get("items","oneprovider_space_namespace_storage_used")

		if config.has_option("items","oneprovider_space_namespace_data_access"):
			self.oneprovider_space_namespace_data_access = config.get("items","oneprovider_space_namespace_data_access")

		if config.has_option("items","oneprovider_space_namespace_block_access"):
			self.oneprovider_space_namespace_block_access = config.get("items","oneprovider_space_namespace_block_access")

		if config.has_option("items","oneprovider_space_namespace_connected_users"):
			self.oneprovider_space_namespace_connected_users = config.get("items","oneprovider_space_namespace_connected_users")

		# iam section

		if config.has_option("iam","urlrefresh"):
			self.AUTH_URLREFRESH = config.get("iam","urlrefresh")
		else:
			logging.error( "There is no urlrefresh option into iam section.")

		# log section

		if config.has_option("log","loglevel"):
			self.LOGLEVEL = config.get("log","loglevel")
		else:
			self.error = "There is no loglevel option into log section."
