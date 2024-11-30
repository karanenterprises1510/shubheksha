package com.shubheksha.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "products")
@ToString
@Getter
@Setter
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PRODUCT_NAME")
	private String productName;

	private String sku;

	private Long category;

	@Column(name = "OFFER_PRICE")
	private Double offerPrice;

	@Column(name = "LIST_PRICE")
	private Double listPrice;

	private String description;

	private Integer identifier;
	
	@Column(name = "PRODUCT_TITLE")
	private String productTitle;

	private String slug;

	@Column(name = "META_DESCRIPTION")
	private String metaDesc;

	@Column(name = "RELATED_PRODUCTS")
	private String relatedProducts;

	@Column(name = "CANONICAL_URL")
	private String canonicalUrl;

	private String keywords;

	private String permalink;

	private String active;

	private Long createdby;

	private Long modifiedby;

	private Date createdate;

	private Date modidate;

}
