package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class CmdbServiceThread<T extends MetricsCollector> extends ProbeThread<T> {

  private static final Log logger = LogFactory.getLog(CmdbServiceThread.class);

  protected CmdbServiceThread(String category, String group, String template) {
    super(category, group, template);
  }

  @Override
  protected List<T> createCollectors() {
    List<T> result = new ArrayList<>();

    CmdbClient cmdbClient = new CmdbClient();
    List<ServiceInfo> mesosServices = cmdbClient.getServiceList(getServiceType());
    for (ServiceInfo service : mesosServices) {
      try {
        T collector = createServiceCollector(service);
        if (collector != null) {
          result.add(createServiceCollector(service));
        }
      } catch (Throwable e) {
        logger.error("Error creating collector for service " + service.getId(), e);
      }
    }

    return result;
  }

  protected abstract T createServiceCollector(ServiceInfo service);

  protected abstract DocDataType.ServiceType getServiceType();
}
