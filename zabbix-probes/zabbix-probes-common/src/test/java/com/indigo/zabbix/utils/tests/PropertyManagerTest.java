package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.PropertiesManager;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jose on 5/04/17.
 */
public class PropertyManagerTest {

  public static final String EXISTING = "existing";
  public static final String NON_EXISTING = "non.existing";
  public static final String VALUE = "value";
  public static final String DEFAULT = "default";

  public static final String LIST = "list";
  public static final String LIST2 = "list2";

  private List<String> defValues = new ArrayList<>();

  @Before
  public void prepare() {
    defValues.add("value_list1");
    defValues.add("value_list2");
  }

  @Test
  public void testConfig() {

    try {
      PropertiesManager.loadProperties(new InputStreamReader(
          this.getClass().getResourceAsStream("/testconfig.properties")));
    } catch (IOException e) {
      e.printStackTrace();
      assert false;
    }

    String value = PropertiesManager.getProperty(EXISTING);
    assert VALUE.equals(value);

    value = PropertiesManager.getProperty(NON_EXISTING);
    assert value == null;

    value = PropertiesManager.getProperty(EXISTING, DEFAULT);
    assert VALUE.equals(value);

    value = PropertiesManager.getProperty(NON_EXISTING, DEFAULT);
    assert DEFAULT.equals(value);

    List<String> values = PropertiesManager.getListProperty(LIST);
    assert values != null;
    assert values.size() == 2;
    assert defValues.containsAll(values);

    values = PropertiesManager.getListProperty(NON_EXISTING);
    assert values == null;
  }

}
