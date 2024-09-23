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

@Getter
@Setter
@Entity
@Table(name = "cart_product_map")
@ToString
public class CartProductMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CART_ID")
	private Long cartId;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;

	private Integer unit;

	@Column(name = "UNIT_PRICE")
	private Double unitPrice;

	@Column(name = "TOTAL_PRICE")
	private Double totalPrice;

	private String active = "Y";

	private Date createdate;

	private Date modidate;

}
