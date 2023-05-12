package com.api.central.portDetailsUpdate;


/**
 * Date 27 june 2022
 * CR 512
 * Author - rupesh
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortDetailsUpdateController {

	@Autowired
	private PortsDetails portDetails;
	
	
	@GetMapping(value = "/updatePortDetails")
	public ResponseEntity<String> getVesselRefreshData(){
		return new ResponseEntity<String>(portDetails.updatePortDetails(), HttpStatus.OK);
	}

}
