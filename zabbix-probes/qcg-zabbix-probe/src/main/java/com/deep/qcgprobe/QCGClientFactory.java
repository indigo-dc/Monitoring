package com.deep.qcgprobe;

import com.deep.zabbix.utils.AuthenticatedQCGFeignClient;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;

/** Created by damian on 19/07/19. */
public class QCGClientFactory { // extends ProbeClientFactory {

  public static QCGFeignClient getQCGClient(String endpoint, String qcgToken) {
    //return getClient(QCGFeignClient.class, endpoint, true, Logger.Level.FULL);
	  Feign.Builder result =
		        Feign.builder()
		            .decoder(new GsonDecoder())
		            .encoder(new GsonEncoder())
		            .logger(new Slf4jLogger())
		            .logLevel(Logger.Level.FULL);
		    
	  result.client(new AuthenticatedQCGFeignClient(qcgToken));    	
		    
	  return result.target(QCGFeignClient.class, endpoint);
  }
}
