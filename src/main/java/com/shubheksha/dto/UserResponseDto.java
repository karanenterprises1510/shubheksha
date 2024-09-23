package com.shubheksha.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String userName;
	private String password;
	private String email;
	private String mobile;
	private String name;
	private String roleDesc;
	private Integer roleId;
}
