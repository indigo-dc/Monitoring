import unittest
import os
import mock
from mock import MagicMock, Mock, patch, call
from loadconfigs import Datacodejson
import onedata
import zabbix
import json

json_zabbix_credentials = {
    'zabbix_user':'any_user',
    'zabbix_password':'any_pass',
    'zabbix_url':'any_url',
    'zabbix_server':'any_server'
}

MCRHEADER = {'macaroon' : 'ANY_MACAROON' }
EXTRA = { 'client_id': 'ANY_CLIENT_ID', 'client_secret': 'ANY_CLIENT_SECRET'}

def get_text_response(filename):
    tmpfile = open(filename,'r')
    txt = tmpfile.read()
    tmpfile.close()
    return str(txt)

class TestOnedataZabbixAgent(unittest.TestCase):
    print '\n'
    print "==============================================================="
    print "---------- starting test for Onedata-Zabbix Agent  ------------"
    print "==============================================================="
    print '\n'


    def test_od_build_url_op(self):
        testurl = 'https://oneprovider.cloud.cnaf.infn.it:8443/api/v3/oneprovider/somemetric'
        onedata.build_url('oneprovider','somemetric')
        assert onedata.build_url('oneprovider','somemetric') == testurl


    def test_od_build_url_oz(self):
        testurl = 'https://onezone.cloud.cnaf.infn.it:8443/api/v3/onezone/anothermetric'
        onedata.build_url('onezone','anothermetric')
        assert onedata.build_url('onezone','anothermetric') == testurl


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_list_of_spaces(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = 'tests/test_files/onezone_user_spaces.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        spaces_list = []
        spaces_list = onedata.get_list_of_spaces(MCRHEADER,EXTRA)
        lenreq = len(spaces_list)
        assert int(lenreq) == 2


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_oz_spaces_spaceid(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = 'tests/test_files/onezone_spaces_space1.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata
        sid = onedata.get_oz_spaces_spaceid('anyspace',MCRHEADER,EXTRA)
        jsonresp = json.loads(sid)
        assert jsonresp['spaceId'] == 'space1_0123'
        assert jsonresp['canonicalName'] == 'space1'


    @mock.patch('onedata.get_privileges_by_group')
    @mock.patch('onedata.get_group_info')
    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_oz_groups_by_space(self, mock_request, mock_url, mock_groupinfo, mock_privileges):
        mock_url.return_value = "any/url"
        mock_groupinfo.return_value = Datacodejson(200,{"groupId":"ASDFQWER1234","name":"Test group","type":"role"})
        mock_privileges.return_value = Datacodejson(200,{ "privileges": ["space_view_data"]})
        fn = 'tests/test_files/onezone_spaces_space1_groups.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata
        vgr = onedata.get_oz_groups_by_space('anyspace',MCRHEADER,EXTRA)
        assert vgr[0] == 'group_1'


    @mock.patch('onedata.get_privileges_by_space_user')
    @mock.patch('onedata.get_space_user_info')
    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_oz_users_by_space(self, mock_request, mock_url, mock_space_user_info, mock_privileges):
        mock_url.return_value = "any/url"
        mock_space_user_info.return_value = Datacodejson(200,
            {
                "userId":"FvDx3ti",
                "name":"Edwin Ortiz",
                "login":"",
                "alias":"",
                "emailList":["eoortiz@indracompany.com"],
                "connectedAccounts":[
                    {"user_id":"904e6668",
                    "provider_id":"indigo",
                    "name":"Edwin Ortiz",
                    "login":"",
                    "email_list":["eoortiz@indracompany.com"]}
                ]
            }
        )
        mock_privileges.return_value = Datacodejson(200,{"privileges":["space_add_provider","space_change_data","space_invite_group","space_invite_user","space_manage_shares","space_remove","space_remove_group","space_remove_provider","space_remove_user","space_set_privileges","space_view_data","space_write_files"]})
        fn = 'tests/test_files/onezone_spaces_space1_users.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata
        vgr = onedata.get_oz_users_by_space('anyspace',MCRHEADER,EXTRA)
        assert 'space_add_provider' in vgr['onezone.spaces.anyspace.users.user3.privileges']
        assert vgr['onezone.spaces.anyspace.users.user1.connectedAccounts'] == 1


    @mock.patch('onedata.get_spaceprovider_info')
    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_oz_providers_by_space(self, mock_request, mock_url, mock_provider):
        mock_url.return_value = "any/url"
        mock_provider.return_value = Datacodejson(200,
            {
                "name" : "Example provider",
                "providerId" : "MDAxNmxvYZjUGlI",
                "urls" : [ "http://beta.onedata.org/provider1", "http://beta.onedata.org/provider2" ],
                "redirectionPoint" : "http://beta.onedata.org/provider2",
                "latitude" : 50.06,
                "longitude" : 19.95
            }
        )
        fn = 'tests/test_files/onezone_spaces_space1_providers.txt'
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata
        vgr = onedata.get_oz_providers_by_space('anyspace',MCRHEADER,EXTRA)
        assert float(vgr['onezone.spaces.anyspace.providers.MDAxNmxvYZjUGlI.latitude']) == 50.06
        assert vgr['onezone.spaces.anyspace.providers.MDAxNmxvYZjUGlI.providerId'] == 'MDAxNmxvYZjUGlI'


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_storage_quota_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "tests/test_files/oneprovider_storage_quota.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata
        qbs = onedata.get_op_storage_quota_by_space('anyspace',MCRHEADER,EXTRA)
        assert float(qbs) == 1000000000


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_storage_quota_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "tests/test_files/oneprovider_storage_used.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        stused = onedata.get_op_storage_used_by_space('anyspace',MCRHEADER,EXTRA)
        assert float(stused) == 6.1


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_connected_users_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "tests/test_files/oneprovider_connected_users.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        connusrs = onedata.get_op_connected_users_by_space('anyspace',MCRHEADER,EXTRA)
        assert float(connusrs) == 3


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_data_access_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "tests/test_files/oneprovider_data_access.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        data_access = {}
        data_access = onedata.get_op_data_access_by_space('anyspace',MCRHEADER,EXTRA)
        assert float(data_access['data_access_read']) == 1.1
        assert float(data_access['data_access_write']) == 2.2


    @mock.patch('onedata.build_url')
    @mock.patch('onedata.request_onedata')
    def test_get_op_block_access_by_space(self, mock_request, mock_url):
        mock_url.return_value = "any/url"
        fn = "tests/test_files/oneprovider_block_access.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        simdata = Datacodejson(200,rjson)
        mock_request.return_value = simdata

        data_access = {}
        data_access = onedata.get_op_block_access_by_space('anyspace',MCRHEADER,EXTRA)
        assert float(data_access['block_access_read']) == 3.1
        assert float(data_access['block_access_write']) == 4.2

# ---------------------------------------------------------
# test zabbix functions
# ---------------------------------------------------------

    @mock.patch('zabbix.request_zabbix')
    def test_getZbxAuthenToken(self, mock_request_zabbix):
        fn = "tests/test_files/token.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        test_token = zabbix.getZbxAuthenToken(json_zabbix_credentials)
        assert rjson["result"] == test_token


    @mock.patch('zabbix.request_zabbix')
    def test_getZbxAuthenToken_noresponse(self, mock_request_zabbix):
        mock_request_zabbix.return_value = None
        test_token = zabbix.getZbxAuthenToken(json_zabbix_credentials)
        # this throws log error:
        # ERROR:root:No token
        # ERROR:root:No template option
        assert test_token == None


    @mock.patch('zabbix.request_zabbix')
    def test_getItemsList(self, mock_request_zabbix):
        fn = "tests/test_files/getItemList.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        test_itemlist = zabbix.getItemsList('anytoken','anyid',json_zabbix_credentials)
        assert str(test_itemlist['onezone.spaces.space1.spaceid']) == "23896"


    @mock.patch('zabbix.request_zabbix')
    def test_getAppList(self, mock_request_zabbix):
        fn = "tests/test_files/getApplicationList.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        test_applist = zabbix.getAppList('anytoken','anyid',json_zabbix_credentials)
        assert str(test_applist['ONEPROVIDER']) == "461"


    @mock.patch('zabbix.request_zabbix')
    def test_getTemplateId(self, mock_request_zabbix):
        fn = "tests/test_files/templateid.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        tid = zabbix.getTemplateId('anytoken','anyid',json_zabbix_credentials)
        assert str(tid) == "10106"


    @mock.patch('zabbix.request_zabbix')
    def test_addItemApplication(self, mock_request_zabbix):
        fn = "tests/test_files/addItemApplication.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        apid = zabbix.addItemApplication('anytoken','anyappid','anyitemid',json_zabbix_credentials)
        assert str(apid) == "465"


    @mock.patch('zabbix.request_zabbix')
    def test_createItem(self, mock_request_zabbix):
        fn = "tests/test_files/createItem.txt"
        rtext = get_text_response(fn)
        rjson = json.loads(rtext)
        mock_request_zabbix.return_value = rjson

        itid = zabbix.createItem('anytoken','anyitemname','anykey','anyhostid','anyparamtype','anyvaltype',json_zabbix_credentials)
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
        retlist = zabbix.get_zabbix_itemlist(tkn,tmpl,json_zabbix_credentials)
        lenr = len(retlist)
        lenlst = len(itlst)
        assert lenr == lenlst

        # No token
        tkn = ''
        retlist = zabbix.get_zabbix_itemlist(tkn,tmpl,json_zabbix_credentials)
        assert retlist == None

        # No template
        tmpl = ''
        tkn = 'asdf0123'
        retlist = zabbix.get_zabbix_itemlist(tkn,tmpl,json_zabbix_credentials)
        assert retlist == None

if __name__ == '__main__':
	unittest.main()
