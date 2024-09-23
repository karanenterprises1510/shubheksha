package com.shubheksha.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shubheksha.dto.OrderRequestDto;
import com.shubheksha.dto.OrderResponseDto;
import com.shubheksha.service.OrderService;
import com.shubheksha.utils.Constant;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("/save")
	public ResponseEntity<?> saveOrder(@RequestBody OrderRequestDto request) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final Long cartId = orderService.saveOrderDetails(request);
			if (ObjectUtils.isNotEmpty(cartId)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, cartId);
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

	@GetMapping("/order-detail")
	public ResponseEntity<?> fetchProductDetail(@RequestParam Long orderId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			OrderResponseDto data = orderService.getOrderDetail(orderId);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, data);
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
	
	@GetMapping("/approve")
	public ResponseEntity<?> approveOrder(@RequestParam Long orderId) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final boolean resp = orderService.approveOrder(orderId);
			if (ObjectUtils.isNotEmpty(resp)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, resp);
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

	@GetMapping("/reject")
	public ResponseEntity<?> rejectOrder(@RequestParam Long orderId) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final boolean resp = orderService.rejectOrder(orderId);
			if (ObjectUtils.isNotEmpty(resp)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, resp);
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