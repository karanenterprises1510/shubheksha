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
public class OrderRequestDto {

	Long cartId;
	Double orderAmt;
	String name;
	String email;
	String mobile;
	String address;
	String city;
	String state;
	Integer pincode;
	String gst;

}