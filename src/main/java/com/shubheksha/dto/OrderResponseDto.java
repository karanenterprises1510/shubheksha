package com.shubheksha.dto;

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
public class OrderResponseDto {

	Long orderId;
	CartResponseDto cartDetails;
	Double orderAmount;
	boolean isApproved;
	Long custId;
	String custName;
	String custPhone;
	String custEmail;
	String custAddress;
	String custState;
	String custCity;
	Integer custPincode;
	String custGst;

}