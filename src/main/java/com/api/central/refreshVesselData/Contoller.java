package com.api.central.refreshVesselData;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Contoller {

	@Autowired
	RefreshVessel refreshVessel;

	@GetMapping(value = "/getUpdatedData")
	public ResponseEntity<Map<String, String>> getVesselRefreshData(){
		return new ResponseEntity<Map<String, String>>(refreshVessel.getUpdatredVesselData(), HttpStatus.OK);
	}

}
