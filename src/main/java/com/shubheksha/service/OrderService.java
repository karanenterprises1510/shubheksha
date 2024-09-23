package com.shubheksha.service;

import com.shubheksha.dto.OrderRequestDto;
import com.shubheksha.dto.OrderResponseDto;

public interface OrderService {

	Long saveOrderDetails(OrderRequestDto request);

	OrderResponseDto getOrderDetail(Long orderId);

	boolean approveOrder(Long orderId);

	boolean rejectOrder(Long orderId);

}