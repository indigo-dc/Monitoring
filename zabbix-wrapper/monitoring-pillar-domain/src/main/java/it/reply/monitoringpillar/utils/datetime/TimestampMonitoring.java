package it.reply.monitoringpillar.utils.datetime;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.DateFromParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.DateToParamRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimestampMonitoring {

  /**
   * Decode unixtime.
   * 
   * @param timestamp
   *          timestamp
   * @return date
   * @throws IllegalArgumentException
   *           exe
   */
  public static String decodUnixTime2Date(long timestamp) throws IllegalArgumentException {

    SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    TimeZone.setDefault(TimeZone.getTimeZone("GMT+2:00"));
    java.util.Date timeNew = new java.util.Date(timestamp * 1000);
    return sdfDate.format(timeNew);
  }

  /**
   * Encode Date.
   * 
   * @param time
   *          time
   * @return long date
   * @throws IllegalArgumentException
   *           exe
   */
  public static Long encodeDate2UnitTime(String time) throws IllegalArgumentException {

    DateFormat dfm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    dfm.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

    long unixtime = 0;

    try {
      unixtime = dfm.parse(time).getTime();
      unixtime = unixtime / 1000;
    } catch (ParseException pe) {
      pe.printStackTrace();
      throw new IllegalArgumentException("Impossible to parser the time");
    }
    return unixtime;
  }

  /**
   * Method for formatting the date from.
   * 
   * @param dateFrom
   *          json filter
   * @return Long date
   * @throws IllegalArgumentException
   *           except
   */
  public static Long getDateFromFormatter(DateFromParamRequest dateFrom)
      throws IllegalArgumentException {

    Long dateFromFormatted = TimestampMonitoring.encodeDate2UnitTime(

        dateFrom.getDay() + "-" + dateFrom.getMonth() + "-" + dateFrom.getYear() + " "
            + dateFrom.getTime().getHh() + ":" + dateFrom.getTime().getMm() + ":"
            + dateFrom.getTime().getSs());
    // boolean sinceStart = dateFrom.getStartTime();
    return dateFromFormatted;

  }

  /**
   * Method for formatting the date To json filter.
   * 
   * @return Long date
   * @throws IllegalArgumentException
   *           except
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
