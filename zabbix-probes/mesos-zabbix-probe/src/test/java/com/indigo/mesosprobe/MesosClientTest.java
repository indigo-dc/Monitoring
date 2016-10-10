package com.indigo.mesosprobe;

import com.indigo.zabbix.utils.PropertiesManager;

import org.junit.Before;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jose on 16/08/16.
 */

public class MesosClientTest {

  @Before
  public void prepare() throws IOException {
    PropertiesManager.loadProperties(
      new InputStreamReader(ClassLoader.getSystemClassLoader()
        .getResourceAsStream(MesosProbeTags.CONFIG_FILE))
    );
  }


}
