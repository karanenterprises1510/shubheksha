package com.shubheksha.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartResponseDto {

	Long cartId;
	List<CartRequestDto> productList;
	Double totalAmount;
	Double totalMRP;
}