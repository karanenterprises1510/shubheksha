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

import com.shubheksha.dto.UserResponseDto;
import com.shubheksha.read.cust.repository.UserDataCustomRepository;
import com.shubheksha.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserDataCustomRepositoryImpl implements UserDataCustomRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterMbJdbcTemplate;

	@Override
	public Page<UserResponseDto> findUserData(Long userId, String userName, String email, String mobile, String name,
			Integer role, Integer pageNo, Integer pageSize, String sortParam, String sortDir) {
		try {
			log.info("UserDataCustomRepositoryImpl getting user data");
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			String queryStringSelect = "SELECT u.ID AS userId, u.USERNAME AS userName, u.PLAIN_PASSWORD AS password, u.EMAIL AS email, u.MOBILE AS mobile,"
					+ " u.NAME AS name, u.ROLE AS roleId, r.ROLE AS roleDesc";
			String queryStringCount = "SELECT COUNT(*) ";
			StringBuilder queryStringFrom = new StringBuilder(
					" FROM users u JOIN role r ON u.ROLE = u.ROLE" + " and u.ACTIVE='Y' where 1=1 ");

			if (!ObjectUtils.isEmpty(userId) && userId > 0) {
				queryStringFrom.append("and u.ID IN (:userId) ");
				namedParameters.addValue("userId", userId);
			} else if (StringUtils.isNotEmpty(email)) {
				queryStringFrom.append("and u.email=:email ");
				namedParameters.addValue("email", email);
			} else if (StringUtils.isNotEmpty(mobile)) {
				queryStringFrom.append("and u.MOBILE=:mobile ");
				namedParameters.addValue("mobile", mobile);
			} else {
				if (StringUtils.isNotEmpty(userName)) {
					queryStringFrom.append("and u.USERNAME LIKE :userName ");
					namedParameters.addValue("userName", "%" + userName + "%");
				}

				if (StringUtils.isNotEmpty(name)) {
					queryStringFrom.append("and u.NAME LIKE :name ");
					namedParameters.addValue("name", "%" + name + "%");
				}

				if (!ObjectUtils.isEmpty(role)) {
					queryStringFrom.append("and u.ROLE=:role ");
					namedParameters.addValue("role", role);
				}

			}

			if (pageSize == null || pageSize <= 0) {
				pageSize = Constant.PAGESIZE;
			}

			queryStringFrom.append("and u.ACTIVE='Y'");
			String queryStringOrder = " ORDER BY "
					+ (!StringUtils.isEmpty(sortParam) && !sortParam.equalsIgnoreCase("createDate") ? sortParam
							: "u.ID")
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

			log.info("final query to fetch user data - {}", finalSelectQuery);
			log.info("parameter values - {}", namedParameters.toString());
			List<UserResponseDto> userData = namedParameterMbJdbcTemplate.query(finalSelectQuery, namedParameters,
					new BeanPropertyRowMapper<>(UserResponseDto.class));
			Long totalCount = namedParameterMbJdbcTemplate.queryForObject(queryStringCount + queryStringFrom,
					namedParameters, Long.class);
			log.info("Users fetched from DB - {}", !CollectionUtils.isEmpty(userData) ? userData.size() : 0);
			log.info("Total users in DB - {}", totalCount);
			return new PageImpl<UserResponseDto>(userData, pageInfo, totalCount);
		} catch (Exception e) {
			log.error("Found exception while getting user data : ", e);
		}
		return null;
	}

}
