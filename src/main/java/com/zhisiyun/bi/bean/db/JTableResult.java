package com.zhisiyun.bi.bean.db;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public class JTableResult {

	private int draw = 1;

	private int recordsTotal;

	private int recordsFiltered;

	@SuppressWarnings("rawtypes")
	private List data;

	private String error;

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getData() {
		return data;
	}

	@SuppressWarnings("rawtypes")
	public void setData(List data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
