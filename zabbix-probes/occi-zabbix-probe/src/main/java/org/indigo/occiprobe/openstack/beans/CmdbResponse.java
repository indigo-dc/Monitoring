package org.indigo.occiprobe.openstack.beans;

import java.util.List;

/**
 * Created by jose on 8/02/17.
 */
public class CmdbResponse<T> {

  Integer total_rows;
  Integer offset;
  List<T> rows;

  public Integer getTotal_rows() {
    return total_rows;
  }

  public void setTotal_rows(Integer total_rows) {
    this.total_rows = total_rows;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public List<T> getRows() {
    return rows;
  }

  public void setRows(List<T> rows) {
    this.rows = rows;
  }
}
