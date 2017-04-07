package org.indigo.occiprobe.openstack;

import org.apache.http.message.BasicHeader;

import cz.cesnet.cloud.occi.api.http.HTTPConnection;
import cz.cesnet.cloud.occi.api.http.auth.NoAuthentication;

/**
 * Created by jose on 4/04/17.
 */
public class TokenOcciAuth extends NoAuthentication {

  private String token;

  public TokenOcciAuth(String token) {
    this.token = token;
  }

  @Override
  public void setConnection(HTTPConnection connection) {
    super.setConnection(connection);
    connection.addHeader(new BasicHeader("X-Auth-Token", token));
  }
}
