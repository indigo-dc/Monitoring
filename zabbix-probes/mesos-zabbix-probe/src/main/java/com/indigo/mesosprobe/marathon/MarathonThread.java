package com.indigo.mesosprobe.marathon;


import com.indigo.mesosprobe.MesosProbeTags;
import com.indigo.zabbix.utils.ProbeThread;


public class MarathonThread extends ProbeThread<MarathonCollector> {

  protected MarathonThread() {
    super("IaaS", "Marathon", "RuntimeTestTemplate");
  }

  public static void main(String[] args) {
    new MarathonThread().run(MesosProbeTags.CONFIG_FILE);
  }

  @Override
  protected MarathonCollector createCollector() {
    return new MarathonCollector();
  }
}