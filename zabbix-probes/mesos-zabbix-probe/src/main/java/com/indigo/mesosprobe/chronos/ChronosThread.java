package com.indigo.mesosprobe.chronos;

import com.indigo.mesosprobe.MesosProbeTags;
import com.indigo.zabbix.utils.ProbeThread;

/**
 * Created by jose on 11/21/16.
 */
public class ChronosThread extends ProbeThread<ChronosCollector> {

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
