package it.reply.monitoringpillar.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ConfigPillar {
  /**
   * Write to log file.
   * 
   * @param message
   *          string
   * @param date
   *          date
   * @throws IOException
   *           ioex.
   */
  public static void writeToLogPillarFile(String message, String date) throws IOException {
    Logger log = LogManager.getLogger(ConfigPillar.class.getName());

    log.info(message, date);
  }
}
