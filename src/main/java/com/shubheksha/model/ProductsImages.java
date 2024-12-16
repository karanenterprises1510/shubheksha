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
@Table(name = "product_images")
@ToString
@Getter
@Setter
public class ProductsImages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Column(name = "IMG_NAME")
	private String imgName;

	@Column(name = "IMG_URL")
	private String imgUrl;
	
	@Column(name = "IMG_CAPTION")
	private String imgCaption;

	@Column(name = "IS_PRIMARY")
	private String isPrimary = "N";

	private String active;

	private Date createdate;

	private Date modidate;

}
