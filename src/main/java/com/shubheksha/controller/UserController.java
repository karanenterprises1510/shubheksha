package com.shubheksha.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shubheksha.dto.UserResponseDto;
import com.shubheksha.service.UserService;
import com.shubheksha.utils.Constant;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/get-users")
	public ResponseEntity<?> getUsers(@RequestParam(required = false) Long userId,
			@RequestParam(required = false) String userName, @RequestParam(required = false) String email,
			@RequestParam(required = false) String mobile, @RequestParam(required = false) String name,
			@RequestParam(required = false) Integer role, @RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortParam,
			@RequestParam(required = false) String sortDir) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final Page<UserResponseDto> data = userService.fetchAllUsers(userId, userName, email, mobile, name, role,
					pageNo, pageSize, sortParam, sortDir);
			if (data != null && data.hasContent()) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.LIST, data.getContent());
				result.put("totalPages", data.getTotalPages());
				result.put("totalUsers", data.getTotalElements());
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

	@GetMapping("/user-detail")
	public ResponseEntity<?> fetchUserDetail(@RequestParam Long userId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			UserResponseDto data = userService.getUserDetails(userId);
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

	@PostMapping("/update-user")
	public ResponseEntity<?> updateUser(@RequestBody UserResponseDto user) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			UserResponseDto data = userService.updateUser(user);
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

	@PostMapping("/add-user")
	public ResponseEntity<?> addUser(@RequestBody UserResponseDto user) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			UserResponseDto data = userService.addUser(user);
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

	@PostMapping("/deactivate-user")
	public ResponseEntity<?> deactivateUser(@RequestParam Long userId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			boolean data = userService.deactivateUser(userId);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = data ? "User Deactivated Successfully" : "User Does not Deactivated";
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