package it.reply.monitoringpillar.utils.datetime;

import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringConstant;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.DateFromParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.DateToParamRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.TimeParamRequest;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

public class FilterTimeRequestHandlerMonitoring {

  private static int yearTo;
  private static int monthTo;
  private static int dayTo;
  private static int hourTo;
  private static int minTo;
  private static int secondsTo;

  private static int ONEDAY = 86400;
  private static int THREEDAYS = 259200;
  private static int ONEWEEK = 604800;
  private static int ONEMONTH = 2592000;
  private static int THREEMONTHS = 7776000;
  private static int TENMINS = 600;

  private static final String INCORRECT_DATE =
      "Incorrect date inserted: DateTo is before than DateFrom";

  public static final Long now = (System.currentTimeMillis() / 1000);
  public static final Long adayPast = (System.currentTimeMillis() / 1000) - ONEDAY;
  public static final Long threeDaysAgo = (System.currentTimeMillis() / 1000) - (THREEDAYS);
  public static final Long oneWeekAgo = (System.currentTimeMillis() / 1000) - (ONEWEEK);
  public static final Long oneMonthAgo = (System.currentTimeMillis() / 1000) - (ONEMONTH);
  public static final Long ninetyDays = (System.currentTimeMillis() / 1000) - (THREEMONTHS);

  public static final Long tenMinsAgo = (System.currentTimeMillis() / 1000) - (TENMINS);

  /**
   * This method checks whether date is in a regular format Check date format.
   * 
   * @param timerequest
   *          filter
   * @return condition
   * @throws IllegalArgumentException
   *           execp
   */
  public static boolean checkDateFormat(FilterTimeRequest timerequest)
      throws IllegalArgumentException {

    int yearfrom = Integer.parseInt(timerequest.getDateFrom().getYear());
    int monthfrom = Integer.parseInt(timerequest.getDateFrom().getMonth());
    int dayfrom = Integer.parseInt(timerequest.getDateFrom().getDay());
    int hourfrom = Integer.parseInt(timerequest.getDateFrom().getTime().getHh());
    int minfrom = Integer.parseInt(timerequest.getDateFrom().getTime().getMm());
    int secondsfrom = Integer.parseInt(timerequest.getDateFrom().getTime().getSs());
    if (timerequest.getDateTo().getYear() != null && timerequest.getDateTo().getMonth() != null
        && timerequest.getDateTo().getDay() != null && (timerequest.getDateTo().getUpToNow() == null
            || timerequest.getDateTo().getUpToNow() == false)) {
      yearTo = Integer.parseInt(timerequest.getDateTo().getYear());
      monthTo = Integer.parseInt(timerequest.getDateTo().getMonth());
      dayTo = Integer.parseInt(timerequest.getDateTo().getDay());
      hourTo = Integer.parseInt(timerequest.getDateTo().getTime().getHh());
      minTo = Integer.parseInt(timerequest.getDateTo().getTime().getMm());
      secondsTo = Integer.parseInt(timerequest.getDateTo().getTime().getSs());
    } else if (timerequest.getDateTo().getUpToNow() != null
        && timerequest.getDateTo().getUpToNow()) {
      DateToParamRequest dateto = new DateToParamRequest();
      String currentDay = LocalDate.now().toString() + " " + LocalTime.now().toString();
      setDateTo(timerequest, currentDay);

    }
    if (yearfrom > Calendar.getInstance().get(Calendar.YEAR) && yearfrom < 2014
        || Integer.parseInt(timerequest.getDateTo().getYear()) > Calendar.getInstance()
            .get(Calendar.YEAR) && Integer.parseInt(timerequest.getDateTo().getYear()) < 2014) {
      throw new IllegalArgumentException("Wrong Date YEAR inserted");
    } else if (monthfrom < 01 && monthfrom > 12
        || (Integer.parseInt(timerequest.getDateTo().getMonth()) < 01 && monthTo > 12)) {
      throw new IllegalArgumentException("Wrong Date MONTH inserted");
    } else if (dayfrom < 01 && dayfrom > 31
        || (Integer.parseInt(timerequest.getDateTo().getDay()) < 01
            && Integer.parseInt(timerequest.getDateTo().getDay()) > 31)) {
      throw new IllegalArgumentException("Wrong Date DAY inserted");
    } else if (hourfrom > 23
        || (Integer.parseInt(timerequest.getDateTo().getTime().getHh()) > 23)) {
      throw new IllegalArgumentException("Wrong Date hour inserted");
    } else if (minfrom > 59 || (Integer.parseInt(timerequest.getDateTo().getTime().getMm()) > 59)) {
      throw new IllegalArgumentException("Wrong Date Minutes  inserted");
    } else if (secondsfrom > 59
        || (Integer.parseInt(timerequest.getDateTo().getTime().getSs()) > 59)) {
      throw new IllegalArgumentException("Wrong Date Seconds inserted");
    } else if (TimestampMonitoring
        .getDateToFormatter(timerequest.getDateTo()) > (System.currentTimeMillis() / 1000L)) {
      throw new IllegalArgumentException("Wrong dateTo inserted: it can't be a future date");
    }
    return true;
  }

  /**
   * Check if the request time is before than 24h, in order to choose between trend and history API.
   *
   * @param requestTime
   *          filter
   * @return condition
   * @throws IllegalArgumentException
   *           exep
   */
  public static boolean isBeforeThen24h(FilterTimeRequest requestTime)
      throws IllegalArgumentException {

    // When using timestamp coming straight from API signature
    Long adayPast = (System.currentTimeMillis() / 1000) - 86400;
    // If dateTo is before dateFrom it throws an Exception
    if (requestTime.getFrom() != null && requestTime.getTo() != null
        && (requestTime.getDateFrom() == null || requestTime.getDateTo() == null)) {
      if (requestTime.getTo() < requestTime.getFrom()) {
        throw new IllegalArgumentException(INCORRECT_DATE);
      }
      // Check the time coming from the request: if longer then 1 day
      // (86400s), then call TREND API
      if ((requestTime.getFrom() < adayPast)
          || (requestTime.getTo() < adayPast) && (requestTime.getTo() > adayPast
              && requestTime.getTo() < (System.currentTimeMillis() / 1000L)
              && requestTime.getFrom() < adayPast)) {
        return true;
      } else {
        return false;
      }

      // When using API as rpc like (human-readable format into
      // parameters)
    } else {
      Long dateFromEncoded = TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom());
      Long dateToEncoded = TimestampMonitoring.getDateToFormatter(requestTime.getDateTo());

      if (dateToEncoded < dateFromEncoded) {
        throw new IllegalArgumentException(INCORRECT_DATE);
      }
      // Check the time coming from the request: if longer then 1 day
      // (86400s), then call TREND API
      if ((requestTime.getDateTo().getUpToNow() != null && requestTime.getDateTo().getUpToNow()
          && dateFromEncoded < adayPast)
          || (dateToEncoded < adayPast)
          || (requestTime.getDateTo().getUpToNow() != null
              && !(requestTime.getDateTo().getUpToNow())
              && (dateToEncoded > adayPast && dateToEncoded < (System.currentTimeMillis() / 1000L)
                  && dateFromEncoded < adayPast))) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Check if the request time is before than 24h, in order to choose between trend and history API.
   * Get the number of samples.
   * 
   * @param requestTime
   *          filter
   * @return numebr of samples
   * @throws IllegalArgumentException
   *           exep
   */
  public static Integer determineHowManySamples(FilterTimeRequest requestTime)
      throws IllegalArgumentException {
    Long dateFromEncoded = null;
    Long dateToEncoded = null;
    
    if ((requestTime.getDateFrom() == null || requestTime.getDateTo() == null)
        && (requestTime.getFrom() != null || requestTime.getTo() != null)) {
      dateFromEncoded = requestTime.getFrom();
      dateToEncoded = requestTime.getTo();
    } else {
      // If dateTo is before dateFrom it throws an Exception
      dateFromEncoded = TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom());
      dateToEncoded = TimestampMonitoring.getDateToFormatter(requestTime.getDateTo());
    }

    if (dateToEncoded < dateFromEncoded) {
      throw new IllegalArgumentException("Incorrect date inserted: DateTo is before than DateFrom");
    }

    if (dateFromEncoded > adayPast && dateFromEncoded < now) {
      return MonitoringConstant.HISTORY_1_DAY;
    } else if ((dateFromEncoded < adayPast) && (dateFromEncoded > threeDaysAgo)) {
      return MonitoringConstant.HISTORY_3_DAYS;
    } else if (dateFromEncoded < threeDaysAgo && dateFromEncoded > oneWeekAgo) {
      return MonitoringConstant.HISTORY_7_DAYS;
    } else if (dateFromEncoded < oneWeekAgo) {
      return MonitoringConstant.HISTORY_90_DAYS;
    } else if (dateFromEncoded < ninetyDays) {
      throw new IllegalArgumentException("No history available for the set time");
    } else {
      throw new IllegalArgumentException("Wrong date format: not acccepted dateFrom");
    }
  }

  /**
   * Set the date from.
   * 
   * @param timeRequest
   *          filter
   * @param dateConverted
   *          converted
   */
  public void setDateFrom(FilterTimeRequest timeRequest, String dateConverted) {

    // set the parameters
    DateFromParamRequest dateFrom = new DateFromParamRequest();
    dateFrom.setDay(dateConverted.substring(0, 2));
    // set the month
    dateFrom.setMonth(dateConverted.substring(3, 5));
    // set the year
    dateFrom.setYear(dateConverted.substring(6, 10));
    TimeParamRequest timeparam = new TimeParamRequest();
    timeparam.setHh(dateConverted.substring(11, 13));
    timeparam.setMm(dateConverted.substring(14, 16));
    timeparam.setSs(dateConverted.substring(17, 19));
    dateFrom.setTime(timeparam);

    timeRequest.setDateFrom(dateFrom);

  }

  /**
   * Set DateTo format.
   * 
   * @param timerequest
   *          pojo
   * @param date
   *          date
   */
  public static void setDateTo(FilterTimeRequest timerequest, String date) {

    DateToParamRequest dateto = new DateToParamRequest();

    // set the day
    dateto.setDay(date.substring(8, 10));
    // set the month
    dateto.setMonth(date.substring(5, 7));
    // set the year
    dateto.setYear(date.substring(0, 4));
    TimeParamRequest timeparam = new TimeParamRequest();
    timeparam.setHh(date.substring(11, 13));
    timeparam.setMm(date.substring(14, 16));
    timeparam.setSs(date.substring(17, 19));
    dateto.setTime(timeparam);
    timerequest.setDateTo(dateto);
  }
}
