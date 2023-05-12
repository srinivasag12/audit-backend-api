package com.api.central.portDetailsUpdate;

/**
 * Date 27 june 2022
 * CR 512
 * Author - rupesh
 */
import java.util.ArrayList;
import java.util.List;

public class ListCertDetails {
	List<RmiCertificateDetails> listDetails = new ArrayList<RmiCertificateDetails>();

	public List<RmiCertificateDetails> getListDetails() {
		return listDetails;
	}

	public void setListDetails(List<RmiCertificateDetails> listDetails) {
		this.listDetails = listDetails;
	}

	@Override
	public String toString() {
		return "ListCertDetails [listDetails=" + listDetails + "]";
	}

}
