package it.reply.monitoringpillar.utility;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.DateFromParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.DateToParamRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimestampMonitoring {

  /**
   * Decode date.
   * 
   * @param timestamp
   *          string
   * @return date
   * @throws IllegalArgumentException
   *           exep
   */
  public static String decodUnixTime2Date(long timestamp) throws IllegalArgumentException {

    SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    java.util.Date timeNew = new Date();
    timeNew = new java.util.Date((long) timestamp * 1000);
    return sdfDate.format(timeNew);
  }

  /**
   * Encode the date.
   * 
   * @param time
   *          pojo
   * @return long date
   * @throws IllegalArgumentException
   *           exep
   */
  public static long encodeDate2UnitTime(String time) throws IllegalArgumentException {

    DateFormat dfm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    dfm.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

    long unixtime = 0;

    try {
      unixtime = dfm.parse(time).getTime();
      unixtime = unixtime / 1000;
      // System.out.println(unixtime);
      // System.out.println(TimeZone.getTimeZone("UTC"));
    } catch (ParseException pe) {
      pe.printStackTrace();
      throw new IllegalArgumentException("Impossible to parser the time");
    }
    return unixtime;
  }

  /**
   * Format the date in human readable format.
   * 
   * @param dateFrom
   *          filter
   * @return Lond date
   * @throws IllegalArgumentException
   *           exep
   */
  public static Long getDateFromFormatter(DateFromParamRequest dateFrom)
      throws IllegalArgumentException {

    Long dateFromFormatted = TimestampMonitoring.encodeDate2UnitTime(

        dateFrom.getDay() + "-" + dateFrom.getMonth() + "-" + dateFrom.getYear() + " "
            + dateFrom.getTime().getHh() + ":" + dateFrom.getTime().getMm() + ":"
            + dateFrom.getTime().getSs());
    return dateFromFormatted;

  }

  /**
   * Format the date.
   * 
   * @param dateToParamRequest
   *          pojo
   * @return long date
   * @throws IllegalArgumentException
   *           exep
   */
  public static Long getDateToFormatter(DateToParamRequest dateToParamRequest)
      throws IllegalArgumentException {

    Long dateToFormatted = TimestampMonitoring.encodeDate2UnitTime(

        dateToParamRequest.getDay() + "-" + dateToParamRequest.getMonth() + "-"
            + dateToParamRequest.getYear() + " " + dateToParamRequest.getTime().getHh() + ":"
            + dateToParamRequest.getTime().getMm() + ":" + dateToParamRequest.getTime().getSs());
    return dateToFormatted;
  }
}