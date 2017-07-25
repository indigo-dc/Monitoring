package org.indigo.openstackprobe.openstack.beans;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 8/02/17.
 */
public class CmdbResponse<T> {

	@SerializedName("total_rows")
	Integer totalRows;
	Integer offset;
	List<T> rows;

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
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
