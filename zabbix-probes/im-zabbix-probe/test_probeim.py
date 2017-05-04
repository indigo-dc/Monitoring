import unittest
import os
import mock
from mock import MagicMock, Mock, patch, call
from loadconfigs import Datacodejson
import tokenmng
import zabbix
import IMinfrastructureOper
import json
import requests
from requests.exceptions import ConnectionError
from requests_oauthlib import OAuth2Session, TokenUpdated

class MockResponse:
    def __init__(self, json_data, status_code):
        self.info = json_data
        self.statuscode = status_code

    def json(self):
        return self.json_data

def get_text_response(filename):
    tmpfile = open(filename,'r')
    txt = tmpfile.read()
    #print "----> " + str(txt)
    tmpfile.close()
    return str(txt)


class TestProbeIMZabbix(unittest.TestCase):
    print '\n'
    print "========================================================="
    print "---------- starting test for probe IM-Zabbix ------------"
    print "========================================================="
    print '\n'

    @mock.patch('IMinfrastructureOper.requestIM')
    def test_list_infrastructure(self,request_get_mock):
        print"\nTesting IMinfrastructureOper.list_infrastructure..."
        request_get_mock.return_value = MockResponse({"key2": "value2"}, 200)
        anyheader = {"headerkey": "headervalue"}
        rl = IMinfrastructureOper.list_infrastructure(anyheader)
        assert rl.statuscode == 200
        assert rl.info == 'list method OK'

    @mock.patch('IMinfrastructureOper.requestIM')
    def test_create_infrastructure(self,request_post_mock):
        print "\nTesting IMinfrastructureOper.create_infrastructure..."
        request_post_mock.return_value = MockResponse('{"uri": "any/url"}', 200)
        anyheader = {"headerkey": "headervalue"}
        rc = IMinfrastructureOper.create_infrastructure(anyheader)
        jsonuri = json.loads(str(request_post_mock.return_value.info))
        assert rc.statuscode == 200
        assert str(jsonuri['uri']) == 'any/url'

    @mock.patch('IMinfrastructureOper.requestIM')
    def test_start_infrastructure(self,request_put_mock):
        print "\nTesting IMinfrastructureOper.start_infrastructure..."
        request_put_mock.return_value = MockResponse({}, 200)
        anyheader = {"headerkey": "headervalue"}
        anyurl = 'any/url'
        rc = IMinfrastructureOper.start_infrastructure(anyheader,anyurl)
        assert rc.statuscode == 200
        assert rc.info == 'start method OK'

    @mock.patch('IMinfrastructureOper.requestIM')
    def test_create_vm(self,request_vmpost_mock):
        print "\nTesting IMinfrastructureOper.create_vm..."
        request_vmpost_mock.return_value = MockResponse({"uri-list": []}, 200)
        anyheader = {"headerkey": "headervalue"}
        anyurl = 'any/url'
        cv = IMinfrastructureOper.create_vm(anyheader,anyurl)
        assert cv.statuscode == 200
        assert cv.info =='Creation of VM is OK'

    @mock.patch('IMinfrastructureOper.requestIM')
    def test_delete_vm(self,request_delete_mock):
        print "\nTesting IMinfrastructureOper.delete_infrastructure..."
        request_delete_mock.return_value = MockResponse({}, 200)
        anyheader = {"headerkey": "headervalue"}
        anyurl = 'any/url'
        dv = IMinfrastructureOper.delete_infrastructure(anyheader,anyurl)
        assert dv.statuscode == 200
        assert dv.info =='delete_infrastructure is OK'

    @mock.patch('zabbix.request_zabbix')
    def test_getItemList(self,reqzbx_mock):
        print "\nTesting zabbix.getItemsList..."
        jsontext = '{"jsonrpc":"2.0","result":[{"itemid":"25372","name":"create_inf","key_":"create_inf","value_type":"4","hostid":"10107"},{"itemid":"25368","name":"create_vm","key_":"create_vm","value_type":"4","hostid":"10107"},{"itemid":"25374","name":"delete_inf","key_":"delete_inf","value_type":"4","hostid":"10107"},{"itemid":"25366","name":"list_inf","key_":"list_inf","value_type":"4","hostid":"10107"},{"itemid":"25378","name":"msg_error","key_":"msg_error","value_type":"4","hostid":"10107"},{"itemid":"25376","name":"num_error","key_":"num_error","value_type":"4","hostid":"10107"},{"itemid":"25370","name":"start_inf","key_":"start_inf","value_type":"4","hostid":"10107"}],"id":1}'
        reqzbx_mock.return_value = json.loads(jsontext)
        anytoken = 'asdfqwerty'
        anyid = 'abcd-0123'
        itemList = zabbix.getItemsList(anytoken,anyid)
        assert itemList['create_inf'] == str(25372)

    @mock.patch('zabbix.request_zabbix')
    def test_getTemplateId(self,reqzbx_mock):
        print "\nTesting zabbix.getTemplateId..."
        jsontext = '{"jsonrpc":"2.0","result":[{"templateid":"10107"}],"id":1}'
        reqzbx_mock.return_value = json.loads(jsontext)
        anytoken = 'asdfqwerty'
        faketemplid = 'my_template'
        tplId = zabbix.getTemplateId(anytoken,faketemplid)
        assert tplId == str(10107)

    @mock.patch('zabbix.request_zabbix')
    def test_getHostId(self,reqzbx_mock):
        print "\nTesting zabbix.getHostId..."
        jsontext = '{"jsonrpc":"2.0","result":[{"hostid":"10105","proxy_hostid":"0","host":"imhost","status":"0","disable_until":"0","error":"","available":"0","errors_from":"0","lastaccess":"0","ipmi_authtype":"-1","ipmi_privilege":"2","ipmi_username":"","ipmi_password":"","ipmi_disable_until":"0","ipmi_available":"0","snmp_disable_until":"0","snmp_available":"0","maintenanceid":"0","maintenance_status":"0","maintenance_type":"0","maintenance_from":"0","ipmi_errors_from":"0","snmp_errors_from":"0","ipmi_error":"","snmp_error":"","jmx_disable_until":"0","jmx_available":"0","jmx_errors_from":"0","jmx_error":"","name":"imhost","flags":"0","templateid":"0","description":"","tls_connect":"1","tls_accept":"1","tls_issuer":"","tls_subject":"","tls_psk_identity":"","tls_psk":""}],"id":1}'
        reqzbx_mock.return_value = json.loads(jsontext)
        anytoken = 'asdfqwerty'
        fakehostname = 'my_host'
        hstId = zabbix.getHostId(anytoken,fakehostname)
        assert hstId == str(10105)

    @mock.patch('zabbix.request_zabbix')
    def test_getZabbixToken(self,reqzbx_mock):
        print "\nTesting zabbix.getZbxAuthenToken..."
        jsontext = '{"jsonrpc":"2.0","result":"5c0f2ce9","id":1}'
        reqzbx_mock.return_value = json.loads(jsontext)
        ztkn = zabbix.getZbxAuthenToken('my_user','my_password')
        assert ztkn == '5c0f2ce9'


if __name__ == '__main__':
	unittest.main()
