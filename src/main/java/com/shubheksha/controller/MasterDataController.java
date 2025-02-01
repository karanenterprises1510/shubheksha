package com.shubheksha.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubheksha.service.MasterDataService;
import com.shubheksha.utils.Constant;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/master")
public class MasterDataController {

	@Autowired
	MasterDataService masterService;

	@GetMapping(value = "/get-master-data", produces = "application/json")
	public ResponseEntity<?> getMasterData() {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final Map<String, String> data = masterService.getAllMasterData();
			if (data != null) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.LIST, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

}