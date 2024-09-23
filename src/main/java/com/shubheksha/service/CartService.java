package com.shubheksha.service;

import java.util.List;

import com.shubheksha.dto.CartRequestDto;
import com.shubheksha.dto.CartResponseDto;

public interface CartService {

	Long saveCartDetails(List<CartRequestDto> request);
	
	CartResponseDto getCartDetail(Long cartId);

}