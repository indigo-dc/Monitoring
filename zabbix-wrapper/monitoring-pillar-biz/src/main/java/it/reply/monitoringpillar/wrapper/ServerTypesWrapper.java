package it.reply.monitoringpillar.wrapper;

import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol.PillarServerTypesResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol.PillarZone4Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ServerTypesWrapper {

  @Inject
  private Configuration config;

  /**
   * Get server types.
   * 
   * @param zoneName
   *          name
   * @return serrver type
   */
  public PillarServerTypesResponse getServerTypesWrapped(String zoneName) {

    PillarZone4Server zoneResponse = new PillarZone4Server();
    List<PillarZone4Server> zonesResponse = new ArrayList<>();
    HashMap<String, List<String>> serversPerZoneMap =
        (HashMap<String, List<String>>) config.getServersPerZone(zoneName);

    zoneResponse.setName(zoneName);
    zoneResponse.setServerTypes(serversPerZoneMap.get(zoneName));
    zonesResponse.add(zoneResponse);

    PillarServerTypesResponse response = new PillarServerTypesResponse();
    response.setPillarzone4Server(zonesResponse);

    return response;

  }

}
