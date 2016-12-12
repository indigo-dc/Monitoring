package com.indigo.mesosprobe.chronos;

import com.indigo.mesosprobe.MesosProbeTags;
import com.indigo.zabbix.utils.CollectorThread;

/**
 * Created by jose on 11/21/16.
 */
public class ChronosThread extends CollectorThread<ChronosCollector> {

  public ChronosThread() {
    super("IaaS", "Chronos", "RuntimeTestTemplate");
  }

  public static void main(String[] args) {
    new ChronosThread().run(MesosProbeTags.CONFIG_FILE);
  }

  @Override
  protected ChronosCollector createCollector() {
    return new ChronosCollector();
  }
}
