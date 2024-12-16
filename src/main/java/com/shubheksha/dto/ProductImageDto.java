package com.shubheksha.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductImageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long imageId;
	private String productImg;
	private String productImgUrl;
	private String productImgCaption;
	private boolean isPrimary;
}
