package com.shubheksha.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productId;
	private String productName;
	private String sku;
	private Long productCategory;
	private String productCatName;
	private Double offerPrice;
	private Double listPrice;
	private String description;
	private String productTitle;
	private String slug;
	private String metaDescription;
	private String relatedProducts;
	private String canonicalUrl;
	private String keywords;
	private String identifier;
	private Integer identifierId;
	private String productImg;
	private String productImgUrl;
	private String productImgCaption;
	private String permalink;
	private String active;
	private Date createDate;
	private Date modiDate;
}
