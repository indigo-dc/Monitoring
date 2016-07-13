package it.reply.monitoringpillar.adapter.zabbix;

import it.reply.monitoringpillar.adapter.zabbix.exception.IllegalArgumentZabbixException;

public class ZabbixConstant {

  public static final String ZABBIX_PORT_AGENT = "10050";

  public static final String METHOD_USER_LOGIN = "user.login";

  public static final Integer ID = 1;

  public static final String EXTEND = "extend";

  public static final int TEXT_HISTORY_TYPE = 4;
  public static final int INT_HISTORY_TYPE = 3;
  public static final int FLOAT_HISTORY_TYPE = 0;

  public static final String WRONGHOSTNAME =
      "Wrong Host Name Inserted: Not Existing Into Platform: ";
  public static final String WRONGGROUPNAME =
      "Wrong Group Name Inserted, Not Existing Into Platform: ";
  public static final String INVENTORY_NOT_SET =
      "Inventory out of START_ARRAY token: tag perhaps not set";

  public static final String TEMPLATE_RADIX_ACTIVE_MODE = "TemplateActive";
  public static final String TEMPLATE_RADIX_PASSIVE_MODE = "Template";
  public static final String TEMPLATE_AGGREGATOR = "TemplateAggregator";

  public static final String ZAB_ADMINS = "Zabbix administrators";

  public static final String PROXY_ACTIVE_PROP = "5";

  public static final String IAAS = "IaaS";

  public static final Integer HISTORY_90_DAYS = 12960;
  public static final Integer HISTORY_30_DAYS = 4320;
  public static final Integer HISTORY_7_DAYS = 1080;
  public static final Integer HISTORY_3_DAYS = 432;
  public static final Integer HISTORY_1_DAY = 144;

  public enum MetricAction {

    ENABLE_HOST_METRICS("enable", 0), DISABLE_HOST_METRIC("disable", 1);

    private String action;
    private int status;

    private MetricAction(String action, int status) {
      this.action = action;
      this.status = status;
    }

    public String getAction() {
      return action;
    }

    public int getStatus() {
      return status;
    }

    /**
     * .
     * 
     * @param update
     *          .
     * @return .
     * @throws IllegalArgumentZabbixException
     *           .
     */
    public static MetricAction lookupFromName(String update) throws IllegalArgumentZabbixException {
      for (MetricAction a : values()) {
        if (update.equals(a.getAction())) {
          return a;
        }
      }
      throw new IllegalArgumentZabbixException("Cannot find [" + update + "] into MetricAction");
    }
  }
}
