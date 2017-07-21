package org.indigo.openstackprobe.openstack.beans;

import java.util.List;

/**
 * Created by jose on 8/02/17.
 */
public class ServiceType {

  String id;
  List<String> key;
  ProviderInfo value;
  DocType doc;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getKey() {
    return key;
  }

  public void setKey(List<String> key) {
    this.key = key;
  }

  public ProviderInfo getValue() {
    return value;
  }

  public void setValue(ProviderInfo value) {
    this.value = value;
  }

  public DocType getDoc() {
    return doc;
  }

  public void setDoc(DocType doc) {
    this.doc = doc;
  }
}