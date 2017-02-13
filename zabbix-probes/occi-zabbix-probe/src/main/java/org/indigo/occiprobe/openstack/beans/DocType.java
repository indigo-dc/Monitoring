package org.indigo.occiprobe.openstack.beans;

/**
 * Created by jose on 8/02/17.
 */
public class DocType {

  String type;

  DocDataType data;


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public DocDataType getData() {
    return data;
  }

  public void setData(DocDataType data) {
    this.data = data;
  }
}
