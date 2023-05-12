package com.api.central.refreshVesselData;

import java.util.List;

public class VesselRefreshList {

	private List<VesselRefresh> list;

	public VesselRefreshList() {
		// TODO Auto-generated constructor stub
	}

	public VesselRefreshList(List<VesselRefresh> list) {
		this.list = list;
	}

	public List<VesselRefresh> getList() {
		return list;
	}

	public void setList(List<VesselRefresh> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "VesselRefreshList [list=" + list + "]";
	}

}
