package com.shubheksha.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CATEGORY_NAME")
	private String categoryName;

	private String slug;

	private String title;

	@Column(name = "PAGE_TITLE")
	private String pageTitle;

	private String description;

	private String keywords;

	@Column(name = "CATEGORY_IMG")
	private String categoryImg;

	@Column(name = "PARENT_CATEGORY")
	private Long parentCategory;

	@Column(name = "HOME_CARD")
	private String homeCard = "Y";
	
	private String active = "Y";

	private Date createdate;

	private Date modidate;

}