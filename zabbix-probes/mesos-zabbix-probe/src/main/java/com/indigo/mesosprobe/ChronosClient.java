package com.indigo.mesosprobe;

import it.infn.ba.indigo.chronos.client.Chronos;
import it.infn.ba.indigo.chronos.client.model.v1.Container;
import it.infn.ba.indigo.chronos.client.model.v1.Job;
import it.infn.ba.indigo.chronos.client.utils.ChronosException;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Date;

public class ChronosClient {

  private static final Log logger = LogFactory.getLog(ChronosClient.class);

  public static final String JOB_NAME = "zabbix-test-job";

  public boolean testChronos() {

   boolean result = false;

   String url = PropertiesManager.getProperty(MesosProbeTags.CHRONOS_ENDPOINT);
   String username = PropertiesManager.getProperty(MesosProbeTags.CHRONOS_USERNAME);
   String password = PropertiesManager.getProperty(MesosProbeTags.CHRONOS_PASSWORD);

   Chronos chronos = it.infn.ba.indigo.chronos.client.ChronosClient.getInstanceWithBasicAuth(
     url, username, password
   );

   try {
     createJob(chronos, new Date(), JOB_NAME);

     Collection<Job> jobStatus = chronos.getJob(JOB_NAME);
     if (!jobStatus.isEmpty()) {
       chronos.deleteJob(JOB_NAME);

       jobStatus = chronos.getJob(JOB_NAME);

       result = jobStatus.isEmpty();
     } else {
       logger.error("Error starting test job "+JOB_NAME);
     }

   } catch (ChronosException e) {
     logger.error("Error creating test job "+JOB_NAME, e);
   }

   return result;

 }

 private Job createJob(Chronos client, Date when, String name) throws ChronosException {
   Job job = new Job();
   job.setSchedule("R/"+ DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(when)+"/PT5M");
   job.setName(name);
   Container container = new Container();
   container.setType("DOCKER");
   container.setImage("busybox");
   job.setContainer(container);
   job.setCpus(0.5);
   job.setMem(512.0);
   job.setCommand("echo hi; sleep 10; echo bye;");

   client.createJob(job);

   return job;
 }

}
