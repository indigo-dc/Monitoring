package it.reply.monitoringpillar.adapter.zabbix;

import it.reply.monitoringpillar.adapter.zabbix.exception.AuthenticationZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.DuplicateResourceZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.NotFoundZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.AuthenticationException;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ClientResponseException;
import it.reply.utils.web.ws.rest.apiclients.zabbix.exception.ZabbixClientException;

public abstract class ZabbixBase {

  /**
   * Convert a ZabbixClientException into a ZabbixException.
   * 
   * @param zabbix
   *          client exception
   * @return Zabbix wrapper exception
   */
  protected ZabbixException handleException(ZabbixClientException ze) {

    if (ze instanceof AuthenticationException) {
      return new AuthenticationZabbixException(ze.getMessage(), ze);
    }
    if (ze instanceof ClientResponseException) {
      ClientResponseException responseException = (ClientResponseException) ze;
      switch (responseException.getStatus()) {
        case NOT_FOUND:
          return new NotFoundZabbixException(ze.getMessage(), ze);
        case CONFLICT:
          return new DuplicateResourceZabbixException(ze.getMessage(), ze);
        default:
          return new ZabbixException(ze.getMessage(), ze);
      }
    }

    return new ZabbixException(ze.getMessage(), ze);
  }

}
