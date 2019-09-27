package com.deep.qcgprobe;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.deep.qcgprobe.beans.QCGResourcesBean;
import com.indigo.zabbix.utils.MetricsCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixMetrics;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;

/** Created by damian on 4/07/19. */
public class QCGCollector implements MetricsCollector {

  private static final Log LOGGER = LogFactory.getLog(QCGCollector.class);
  private String qcgAPIUrl;
  private String hostname;
  private String serviceId;
  private String token;



public QCGCollector(String masterUrl) {
    this.qcgAPIUrl = masterUrl;
    //this.hostname = findHostName(masterUrl);
  }

  public QCGCollector(ServiceInfo service) {
    this.qcgAPIUrl = service.getDoc().getData().getEndpoint();
    this.hostname = service.getDoc().getData().getProviderId();
    this.serviceId = service.getId();
  }
  public QCGCollector(ServiceInfo service, String token) {
	    this.qcgAPIUrl = service.getDoc().getData().getEndpoint();
	    this.hostname = service.getDoc().getData().getProviderId();
	    this.serviceId = service.getId();
	    this.token = token;
	  }
  
  public String getToken() {
	return token;
  }

  public void setToken(String token) {
	this.token = token;
  }
  
  private Map<String, String> readMetrics(QCGResourcesBean metrics, List<String> metricPropertiesList) {
	  Map<String, String> outputMetricMap = new HashMap<>();  
	  if (metrics != null
			&& metricPropertiesList != null && !metricPropertiesList.isEmpty()  
	        && metrics.getNodes().getNodeStats().getTotal() != null
	        && metrics.getQueues().getJobStats().getTotal() != null) {
	      try {
	    	  outputMetricMap.put(metricPropertiesList.get(metricPropertiesList.indexOf(QCGProbeTags.QCG_METRIC_API_AVAILABILITY)), new String("1"));
	    	  outputMetricMap.put(metricPropertiesList.get(metricPropertiesList.indexOf(QCGProbeTags.QCG_METRIC_CPUS_TOTAL)), Long.toString(metrics.getNodes().getNodeStats().getTotal().getCpus()));
	    	  outputMetricMap.put(metricPropertiesList.get(metricPropertiesList.indexOf(QCGProbeTags.QCG_METRIC_CPUS_USED)), Long.toString(metrics.getQueues().getJobStats().getTotal().getCpus()));
	      } catch (IndexOutOfBoundsException e) {
	    	  e.printStackTrace();
	      }
	    }
	    return outputMetricMap;
	  }
  
  
  @Override
  public ZabbixMetrics getMetrics() {
    List<String> metricProperties = PropertiesManager.getListProperty(QCGProbeTags.QCG_METRICS);
    String qcgToken = PropertiesManager.getProperty(QCGProbeTags.QCG_TOKEN);
    
        if (metricProperties != null && !metricProperties.isEmpty() && qcgToken != null) {
	      QCGFeignClient qcgClient = QCGClientFactory.getQCGClient(qcgAPIUrl, qcgToken);
	      QCGResourcesBean metrics = qcgClient.getResources();
	      ZabbixMetrics result = new ZabbixMetrics();
	
	      result.setHostName(this.getHostName());
	      result.setHostGroup(this.getGroup());
	      result.setServiceType(this.getServiceType());
	
	
	      result.setMetrics(readMetrics(metrics, metricProperties));
	
	      result.setTimestamp(new Date().getTime());
	
	      return result;
	    }
	
	return null;
  }

  @Override
  public String getHostName() {
    return this.serviceId;
  }

  @Override
  public String getGroup() {
    return this.hostname;
  }

  @Override
  public DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.QCG;
  }
}
