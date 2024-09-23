package com.shubheksha.controller;

import java.util.HashMap;
import java.util.List;
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

import com.shubheksha.dto.CartRequestDto;
import com.shubheksha.dto.CartResponseDto;
import com.shubheksha.service.CartService;
import com.shubheksha.utils.Constant;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "cart")
public class CartController {

	@Autowired
	CartService cartService;

	@PostMapping("/save")
	public ResponseEntity<?> saveCart(@RequestBody List<CartRequestDto> request) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final Long cartId = cartService.saveCartDetails(request);
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

	@GetMapping("/cart-detail")
	public ResponseEntity<?> fetchProductDetail(@RequestParam Long cartId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			CartResponseDto data = cartService.getCartDetail(cartId);
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

}