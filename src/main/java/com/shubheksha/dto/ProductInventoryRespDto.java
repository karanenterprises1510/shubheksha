package com.shubheksha.dto;

import lombok.Data;

@Data
public class ProductInventoryRespDto {
	private Long productId;
	private String productName;
	private Long categoryId;
	private String categoryName;
	private String sku;
	private Integer unit;
}
