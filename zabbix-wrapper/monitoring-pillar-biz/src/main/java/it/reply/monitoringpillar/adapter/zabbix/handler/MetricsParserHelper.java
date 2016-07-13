package it.reply.monitoringpillar.adapter.zabbix.handler;

public class MetricsParserHelper {

  // To be used only in Paas platform where float values are supposed to be
  // used
  /**
   * Get Metric Parsed.
   * 
   * @param metricValueType
   *          value
   * @param metricValue
   *          value
   * @return float
   * @throws NumberFormatException
   *           exe
   */
  public static Float getMetricParsedValue(String metricValueType, String metricValue)
      throws NumberFormatException {

    if (metricValueType.equals("0")) {
      // metricValueType = "FLOAT";
      return Float.parseFloat(metricValue);
    } else if (metricValueType.equals("3")) {
      // metricValueType = "NUMERIC UNSIGNED;
      return Float.parseFloat(metricValue);
    } else if (metricValueType.equals("4") || metricValueType.equals("1")
        || metricValueType.equals("2")) {
      // metricValueType = "TEXT"; in case of error (when the metric can't
      // be retrieved because of error)
      try {
        return Float.parseFloat(metricValue);
      } catch (NumberFormatException ne) {
        Float error = (float) -1;
        System.out.println(ne.getMessage());
        return error;
      }
    } else {
      throw new IllegalArgumentException("Not existing passed argument type");
    }
  }

  // IN Case of Server infrastructure one has to manage all the situations
  // (types of values..)
  // that's why it return an object
  /**
   * Get metrics iaas.
   * 
   * @param metricValueType
   *          value
   * @param metricValue
   *          value
   * @return object
   */
  public static Object getMetricIaaSParsedValue(String metricValueType, String metricValue) {

    if (metricValueType.equals("0")) {
      metricValueType = "FLOAT";
      return Float.parseFloat(metricValue);
    } else if (metricValueType.equals("1")) {
      metricValueType = "CHARACTER";
      return metricValue;
    } else if (metricValueType.equals("2")) {
      metricValueType = "LOG";
      return metricValue;
    } else if (metricValueType.equals("3")) {
      metricValueType = "NUMERIC UNSIGNED";
      try {
        return Float.parseFloat(metricValue);
      } catch (NumberFormatException nfe) {
        throw new NumberFormatException();
      }
    } else if (metricValueType.equals("4")) {
      metricValueType = "TEXT";
      return metricValue;
    } else {
      throw new IllegalArgumentException("Not existing passed argument type");
    }
  }
}