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

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CUSTOMER_ID")
	private Long customerId;

	@Column(name = "ORDER_AMT")
	private Double orderAmt;

	private String approved = "N";

	@Column(name = "CART_ID")
	private Long cartId;

	private String active = "Y";

	private Long createdby;

	private Long modifiedby;

	private Date createdate;

	private Date modidate;

}
