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

import com.shubheksha.dto.CategoryResponseDto;
import com.shubheksha.read.cust.repository.CategoryCustomRepository;
import com.shubheksha.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterMbJdbcTemplate;

	@Override
	public Page<CategoryResponseDto> findCategoryData(Long categoryId, String categoryName, String slug, String title,
			String pageTitle, String description, String keywords, Integer pageNo, Integer pageSize, String sortParam,
			String sortDir) {
		try {
			log.info("CategoryCustomRepositoryImpl getting category data");
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			String queryStringSelect = "SELECT c.ID AS categoryId, c.CATEGORY_NAME AS categoryName, pc.ID AS parentId, pc.CATEGORY_NAME AS parentName, c.SLUG AS catSlug, c.TITLE AS catTitle, c.PAGE_TITLE AS pageTitle,"
					+ " c.DESCRIPTION AS description, c.KEYWORDS AS keywords ";
			String queryStringCount = "SELECT COUNT(*) ";
			StringBuilder queryStringFrom = new StringBuilder(
					" FROM category c LEFT JOIN category pc ON pc.ID = c.PARENT_CATEGORY" + " and pc.ACTIVE='Y' where 1=1 ");

			if (!ObjectUtils.isEmpty(categoryId) && categoryId > 0) {
				queryStringFrom.append("and c.ID IN (:categoryId) ");
				namedParameters.addValue("categoryId", categoryId);
			} else {
				if (StringUtils.isNotEmpty(categoryName)) {
					queryStringFrom.append("and c.CATEGORY_NAME LIKE :categoryName ");
					namedParameters.addValue("categoryName", "%" + categoryName + "%");
				}

				if (StringUtils.isNotEmpty(slug)) {
					queryStringFrom.append("and c.SLUG LIKE :slug ");
					namedParameters.addValue("slug", "%" + slug + "%");
				}

				if (StringUtils.isNotEmpty(title)) {
					queryStringFrom.append("and c.TITLE LIKE :title ");
					namedParameters.addValue("title", "%" + title + "%");
				}

				if (StringUtils.isNotEmpty(pageTitle)) {
					queryStringFrom.append("and c.PAGE_TITLE LIKE :pageTitle ");
					namedParameters.addValue("pageTitle", "%" + pageTitle + "%");
				}

				if (StringUtils.isNotEmpty(description)) {
					queryStringFrom.append("and c.DESCRIPTION LIKE :description ");
					namedParameters.addValue("description", "%" + description + "%");
				}

				if (StringUtils.isNotEmpty(keywords)) {
					queryStringFrom.append("and c.KEYWORDS LIKE :keywords ");
					namedParameters.addValue("keywords", "%" + keywords + "%");
				}

			}

			if (pageSize == null || pageSize <= 0) {
				pageSize = Constant.PAGESIZE;
			}

			queryStringFrom.append("and c.ACTIVE='Y'");
			String queryStringOrder = " ORDER BY "
					+ (!StringUtils.isEmpty(sortParam) && !sortParam.equalsIgnoreCase("createDate") ? sortParam
							: "c.ID")
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

			log.info("final query to fetch category data - {}", finalSelectQuery);
			log.info("parameter values - {}", namedParameters.toString());
			List<CategoryResponseDto> userData = namedParameterMbJdbcTemplate.query(finalSelectQuery, namedParameters,
					new BeanPropertyRowMapper<>(CategoryResponseDto.class));
			Long totalCount = namedParameterMbJdbcTemplate.queryForObject(queryStringCount + queryStringFrom,
					namedParameters, Long.class);
			log.info("Categories fetched from DB - {}", !CollectionUtils.isEmpty(userData) ? userData.size() : 0);
			log.info("Total categories in DB - {}", totalCount);
			return new PageImpl<CategoryResponseDto>(userData, pageInfo, totalCount);
		} catch (Exception e) {
			log.error("Found exception while getting category data : ", e);
		}
		return null;
	}

}
