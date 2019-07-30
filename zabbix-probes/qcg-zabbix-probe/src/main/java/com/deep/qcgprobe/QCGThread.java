package com.deep.qcgprobe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.indigo.zabbix.utils.CmdbServiceThread;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;

/** Created by damian on 22/07/19. */
public class QCGThread extends CmdbServiceThread<QCGCollector> {

  private static final Log logger = LogFactory.getLog(QCGThread.class);

  protected QCGThread() {
    super("IaaS", "Cloud_Providers", "TemplateQCG");
  }

  /**
   * Start monitoring process.
   *
   * @param args Arguments will be ignored.
   */
  public static void main(String[] args) {
    new QCGThread().run(QCGProbeTags.CONFIG_FILE, args);
	  
  }

  @Override
  protected QCGCollector createServiceCollector(ServiceInfo service) {
    return new QCGCollector(service);
  }
  
  @Override
  protected DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.QCG;
  }
}
