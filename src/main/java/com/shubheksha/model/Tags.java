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
@Table(name = "tags")
public class Tags {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private String slug;

	@Column(name = "PAGE_DESCRIPTION")
	private String pageDesc;
	
	@Column(name = "PAGE_TITLE")
	private String pageTitle;
	
	@Column(name = "PAGE_KEYWORDS")
	private String pageKeywords;

	private String active;
	
	private Date createdate;

	private Date modidate;

}
