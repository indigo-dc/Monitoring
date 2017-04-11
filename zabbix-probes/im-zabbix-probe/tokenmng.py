import json
import time
import sys
import requests
from requests.exceptions import ConnectionError
from requests_oauthlib import OAuth2Session, TokenUpdated

import logging
# from logging.handlers import RotatingFileHandler


def get_newtoken(client_id,urlref,token,refresh_header):
    logging.warning("getting newtoken")

    try:
        session = OAuth2Session(
        client_id,
        token=token,
        auto_refresh_url=urlref,
        auto_refresh_kwargs=refresh_header)

        r = session.get(urlref)
        logging.info(str(r))
    except TokenUpdated as e:
        logging.info("TokenUpdated exception")
        return e

def update_imheaders(tokenstring):
    authoriz = "id = os; type = OpenNebula; host = http://onecloud.i3m.upv.es:2633; token = "+tokenstring+"\\n id = im; type = InfrastructureManager; token = " + tokenstring + " ;"

    UPD_HEADERS = {
        "Content-Type" : "text/plain",
        "Accept": "application/json",
        "Authorization" : authoriz
    }

    return UPD_HEADERS

def update_tokenjson(access_token,ref_token):

    json_token = {
        'access_token': access_token,
        'token_type': 'Bearer',
        'refresh_token': ref_token,
        'expires_in': '-30'
    }

    return json_token


def gettokeninfo_from_file(token_file):
    # open token file and extract access token and refresh
    try:
        filehandler = open(token_file,"r")
        filecont = filehandler.read()
        filehandler.close()
        return filecont
    except IOError as e:
        print "I/O error({0}): {1}".format(e.errno, e.strerror)
        sys.exit()
    except:
        print "Unexpected error:", sys.exc_info()[0]
        raise
        return None


def getClientTokenInfoFromArgs(args):


    mainJsonArgs = {}

    if args.token_file == None:

        if args.token == None and args.token_refresh == None:
            print "There is no token_file. You can use --token AND --token_refresh with valid string tokens instead."
            sys.exit()
        elif args.token == None:
            print "Use -t or --token option to pass a valid token."
            sys.exit()
        elif args.token_refresh == None:
            print "Use -r or --token_refresh option to pass a valid refresh token."
            sys.exit()

    if args.token_file:
        filecont = gettokeninfo_from_file(args.token_file)
        try:
            TKJSON = json.loads(filecont)
            mainJsonArgs["REFRESH_TOKEN"] = TKJSON["refresh_token"]
            mainJsonArgs["GLOBAL_ACCESS_TOKEN"] = TKJSON["access_token"]
        except ValueError,e:
            mainJsonArgs["REFRESH_TOKEN"] = None
            mainJsonArgs["GLOBAL_ACCESS_TOKEN"] = None
    elif args.token and args.token_refresh:
        mainJsonArgs["GLOBAL_ACCESS_TOKEN"] = args.token
        mainJsonArgs["REFRESH_TOKEN"] = args.token_refresh
    else:
        print"Error: no token/refresh_token into ",args.token_file
        sys.exit()


    mainJsonArgs["CLIENT_ID"] = args.client_id
    mainJsonArgs["CLIENT_SECRET"] = args.client_secret

    return mainJsonArgs
