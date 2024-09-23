package com.shubheksha.service;

import org.springframework.data.domain.Page;

import com.shubheksha.dto.UserResponseDto;

public interface UserService {

	Page<UserResponseDto> fetchAllUsers(final Long userId, final String userName, final String email,
			final String mobile, final String name, final Integer role, Integer pageNo, Integer pageSize,
			String sortParam, String sortDir);

	UserResponseDto getUserDetails(final Long userId);

	UserResponseDto updateUser(UserResponseDto request);

	boolean deactivateUser(final Long userId);

	UserResponseDto addUser(UserResponseDto user);
}