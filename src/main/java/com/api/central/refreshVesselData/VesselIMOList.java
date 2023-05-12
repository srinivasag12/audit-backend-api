package com.api.central.refreshVesselData;

import java.util.ArrayList;
import java.util.List;

public class VesselIMOList {
	List<Integer> imos = new ArrayList<Integer>();

	public List<Integer> getImos() {
		return imos;
	}

	public void setImos(List<Integer> imos) {
		this.imos = imos;
	}

	@Override
	public String toString() {
		return "VesselIMOList [imos=" + imos + "]";
	}

}
