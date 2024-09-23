package com.shubheksha.read.cust.repository;

import org.springframework.data.domain.Page;

import com.shubheksha.dto.UserResponseDto;

public interface UserDataCustomRepository {

	Page<UserResponseDto> findUserData(Long userId, String userName, String email, String mobile, String name,
			Integer role, Integer pageNo, Integer pageSize, String sortParam, String sortDir);
}
