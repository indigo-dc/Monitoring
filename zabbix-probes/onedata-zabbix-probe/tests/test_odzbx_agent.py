import unittest
import os
import mock
from mock import MagicMock, Mock, patch, call
from loadconfigs import Datacodejson
import onedata
import zabbix
import json

def get_text_response(filename):
    tmpfile = open(filename,'r')
    txt = tmpfile.read()
    #print "----> " + str(txt)
    tmpfile.close()
    return str(txt)

class TestOnedataZabbixAgent(unittest.TestCase):
    print '\n'
    print "==============================================================="
    print "---------- starting test for Onedata-Zabbix Agent  ------------"
    print "==============================================================="
    print '\n'


    def test_od_build_url_op(self):
        testurl = 'https://172.17.0.8:8443/api/v3/oneprovider/somemetric'
        assert onedata.build_url('oneprovider','somemetric') == testurl


    def test_od_build_url_oz(self):
        testurl = 'https://172.17.0.5:8443/api/v3/onezone/anothermetric'
        assert onedata.build_url('onezone','anothermetric') == testurl


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_list_of_spaces(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = 'test_files/onezone_user_spaces.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        spaces_list = []
        spaces_list = onedata.get_list_of_spaces()
        lenreq = len(spaces_list)
        assert int(lenreq) == 2


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_oz_spaces_spaceid(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = 'test_files/onezone_spaces_space1.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        sid = onedata.get_oz_spaces_spaceid('anyspace')
        assert sid == 'space1_0123'


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_storage_quota_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "test_files/oneprovider_storage_quota.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        qbs = onedata.get_op_storage_quota_by_space('anyspace')
        assert float(qbs) == 1000000000


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_storage_quota_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "test_files/oneprovider_storage_used.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        stused = onedata.get_op_storage_used_by_space('anyspace')
        assert float(stused) == 6.1


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_connected_users_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "test_files/oneprovider_connected_users.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        connusrs = onedata.get_op_connected_users_by_space('anyspace')
        assert float(connusrs) == 3


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_data_access_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "test_files/oneprovider_data_access.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        data_access = {}
        data_access = onedata.get_op_data_access_by_space('anyspace')
        assert float(data_access['data_access_read']) == 1.1
        assert float(data_access['data_access_write']) == 2.2


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_block_access_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "test_files/oneprovider_block_access.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        data_access = {}
        data_access = onedata.get_op_block_access_by_space('anyspace')
        assert float(data_access['block_access_read']) == 3.1
        assert float(data_access['block_access_write']) == 4.2

# ---------------------------------------------------------
# test zabbix functions
# ---------------------------------------------------------

    @mock.patch('zabbix.request_zabbix')
    def test_getZbxAuthenToken(self, mock_request_zabbix):
        fn = "test_files/token.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        test_token = zabbix.getZbxAuthenToken('anyuser','anypass')
        assert rjson["result"] == test_token


    @mock.patch('zabbix.request_zabbix')
    def test_getZbxAuthenToken_noresponse(self, mock_request_zabbix):
        mock_request_zabbix.return_value = None
        test_token = zabbix.getZbxAuthenToken('anyuser','anypass')
        # this throws log error:
        # ERROR:root:No token
        # ERROR:root:No template option
        assert test_token == None


    @mock.patch('zabbix.request_zabbix')
    def test_getItemsList(self, mock_request_zabbix):
        fn = "test_files/getItemList.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        test_itemlist = zabbix.getItemsList('anytoken','anyid')
        assert str(test_itemlist['onezone.spaces.space1.spaceid']) == "23896"


    @mock.patch('zabbix.request_zabbix')
    def test_getAppList(self, mock_request_zabbix):
        fn = "test_files/getApplicationList.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        test_applist = zabbix.getAppList('anytoken','anyid')
        assert str(test_applist['ONEPROVIDER']) == "461"


    @mock.patch('zabbix.request_zabbix')
    def test_getTemplateId(self, mock_request_zabbix):
        fn = "test_files/templateid.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        tid = zabbix.getTemplateId('anytoken','anyid')
        assert str(tid) == "10106"

    @mock.patch('zabbix.request_zabbix')
    def test_addItemApplication(self, mock_request_zabbix):
        fn = "test_files/addItemApplication.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        apid = zabbix.addItemApplication('anytoken','anyappid','anyitemid')
        assert str(apid) == "465"


    @mock.patch('zabbix.request_zabbix')
    def test_createItem(self, mock_request_zabbix):
        fn = "test_files/createItem.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        itid = zabbix.createItem('anytoken','anyitemname','anykey','anyhostid','anyparamtype','anyvaltype')
        assert str(itid) == "23896"


    @mock.patch('zabbix.getTemplateId')
    @mock.patch('zabbix.getItemsList')
    def test_get_zabbix_itemlist(self, mock_getItemList, mock_getTemplateId):
        itlst=["item1","item2","item3"]
        mock_getTemplateId.return_value = '12345'
        mock_getItemList.return_value = itlst

        # good way, token and template
        tmpl = 'mytemplate'
        tkn = 'asdf0123'
        retlist = []
        retlist = zabbix.get_zabbix_itemlist(tkn,tmpl)
        lenr = len(retlist)
        lenlst = len(itlst)
        assert lenr == lenlst

        # No token
        print"No token test:"
        tkn = ''
        retlist = zabbix.get_zabbix_itemlist(tkn,tmpl)
        assert retlist == None

        # No template
        print"No template test:"
        tmpl = ''
        tkn = 'asdf0123'
        retlist = zabbix.get_zabbix_itemlist(tkn,tmpl)
        assert retlist == None



if __name__ == '__main__':
	unittest.main()
