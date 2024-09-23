package com.shubheksha.read.cust.impl.repository;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.shubheksha.dto.ProductResponseDto;
import com.shubheksha.read.cust.repository.ProductDataCustomRepository;
import com.shubheksha.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductDataCustomRepositoryImpl implements ProductDataCustomRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterMbJdbcTemplate;

	@Override
	public Page<ProductResponseDto> findProductData(Long categoryId, String productName, Integer sku, Double offerPrice,
			Double listPrice, String keywords, Integer pageNo, Integer pageSize, String sortParam, String sortDir) {
		try {
			log.info("ProductDataCustomRepositoryImpl getting product data");
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			String queryStringSelect = "SELECT pr.ID as productId, pr.PRODUCT_NAME as productName, pr.SKU as sku, pr.CATEGORY as productCategory, "
					+ "ct.CATEGORY_NAME as productCatName, pr.OFFER_PRICE as offerPrice, pr.LIST_PRICE as listPrice, pr.DESCRIPTION as description, "
					+ "pr.PRODUCT_TITLE as productTitle, pr.SLUG as slug, pr.META_DESCRIPTION as metaDescription, pr.RELATED_PRODUCTS as relatedProducts, "
					+ "pr.CANONICAL_URL as canonicalUrl, pr.KEYWORDS as keywords, pr.PRODUCT_IMG as productImg, pr.PRODUCT_IMG_URL AS productImgUrl, "
					+ "pr.PRODUCT_IMG_CAPTION as productImgCaption, pr.PERMALINK as permalink, pr.ACTIVE as active, pr.CREATEDATE as createDate, pr.MODIDATE as modiDate ";
			String queryStringCount = "SELECT COUNT(*) ";
			StringBuilder queryStringFrom = new StringBuilder(
					" from products pr join category ct on pr.CATEGORY=ct.ID" + " and ct.ACTIVE='Y' where 1=1 ");

			if (!ObjectUtils.isEmpty(categoryId)) {
				queryStringFrom.append("and pr.CATEGORY IN (:category) ");
				namedParameters.addValue("category", categoryId);
			}

			if (StringUtils.isNotEmpty(productName)) {
				queryStringFrom.append("and pr.PRODUCT_NAME LIKE :productName ");
				namedParameters.addValue("productName", "%" + productName + "%");
			}

			if (!ObjectUtils.isEmpty(sku)) {
				queryStringFrom.append("and pr.SKU=:sku ");
				namedParameters.addValue("sku", sku);
			}

			if (!ObjectUtils.isEmpty(offerPrice)) {
				queryStringFrom.append("and pr.MBDLD_CAMPAIGN=:offerPrice ");
				namedParameters.addValue("OFFER_PRICE", offerPrice);
			}

			if (!ObjectUtils.isEmpty(listPrice)) {
				queryStringFrom.append("and pr.LIST_PRICE=:listPrice ");
				namedParameters.addValue("listPrice", listPrice);
			}

			if (!StringUtils.isEmpty(keywords)) {
				queryStringFrom.append("and pr.KEYWORDS like :keywords ");
				namedParameters.addValue("keywords", "%" + keywords + "%");
			}

			if (pageSize == null || pageSize <= 0) {
				pageSize = Constant.PAGESIZE;
			}

			queryStringFrom.append("and pr.ACTIVE='Y'");
			String queryStringOrder = " ORDER BY "
					+ (!StringUtils.isEmpty(sortParam) && !sortParam.equalsIgnoreCase("createDate") ? sortParam
							: "pr.ID")
					+ " " + ("asc".equalsIgnoreCase(sortDir) ? sortDir : "DESC");
			String pagination = "";

			Pageable pageInfo = (pageNo == null || pageSize == null) ? Pageable.unpaged()
					: PageRequest.of(pageNo - 1, pageSize);

			if (pageInfo.isPaged()) {
				pagination = " LIMIT :pageSize OFFSET :offset";
				namedParameters.addValue("pageSize", pageInfo.getPageSize());
				namedParameters.addValue("offset", pageInfo.getOffset());
			}

			String finalSelectQuery = queryStringSelect + queryStringFrom + queryStringOrder + pagination;

			log.info("final query to fetch product data - {}", finalSelectQuery);
			log.info("parameter values - {}", namedParameters.toString());
			List<ProductResponseDto> productData = namedParameterMbJdbcTemplate.query(finalSelectQuery, namedParameters,
					new BeanPropertyRowMapper<>(ProductResponseDto.class));
			Long totalCount = namedParameterMbJdbcTemplate.queryForObject(queryStringCount + queryStringFrom,
					namedParameters, Long.class);
			log.info("Products fetched from DB - {}", !CollectionUtils.isEmpty(productData) ? productData.size() : 0);
			log.info("Total products in DB - {}", totalCount);
			return new PageImpl<ProductResponseDto>(productData, pageInfo, totalCount);
		} catch (Exception e) {
			log.error("Found exception while getting products data : ", e);
		}
		return null;
	}

}
