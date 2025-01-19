package com.shubheksha.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Constant {

	public static final String ACCOUNT_SID = "ACbeb1d69ad97f0435dcab8ab33e332cc0";
	public static final String AUTH_TOKEN = "dad1d750e388dbbdfe66df2c86c5d6a8";

	// ------------------------------------------ RESPONSE KEY
	// -------------------------------------
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String DATA = "data";
	public static final String LIST = "list";

	// ------------------------------------------ STATUS CODE
	// -------------------------------------

	public static final String OK = "200";
	public static final String BAD_REQUEST = "400";
	public static final String NOT_AUTHORIZED = "401";
	public static final String FORBIDDEN = "403";
	public static final String NOT_FOUND = "404";
	public static final String SERVER_ERROR = "500";
	public static final String NO_DATA = "204";
	public static final String CONFLICT = "409";

//--------------------------------------END STATUS CODE---------------------------------------

	public static final String RESPONSE_SUCCESS_MESSAGE = "Success";
	public static final String RESPONSE_UNSUCCESS_MESSAGE = "Something went wrong!!";
	public static final String BAD_REQUEST_MESSAGE = "Bad request";
	public static final String NO_DATA_MESSAGE = "No data found";

	// --------------------------------------REGEX
	// PATTERN---------------------------------------
	public static final String EMAILID_REGEX = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*{1}";
	public static final String MOBILENUMBER_REGEX = "[6-9]{1}[0-9]{9}";

	public static final Integer PAGESIZE = 20;

	public static final String YES = "Y";

	public static final String NO = "N";

	public static final String REJECTED = "R";

	public static final String SELLER_MOBILE = "9821377646";

	public static final String INDIAN_ISD = "+91";

	public static final String SHUBHEKSHA_FROM_EMAIL_ID = "thewholesalestreet@gmail.com";

	public static final String SHUBHEKSHA_USERNAME = "thewholesalestreet";

	public static final String SHUBHEKSHA_PWD = "asfo qkom tmkw auhx ";

	public static final String SHUBHEKSHA_SELLER_EMAIL_ID = "wholesalestreetorders@gmail.com";

	public static boolean isValidEmailId(String emailId) {
		if (StringUtils.isNotBlank(emailId)) {
			Pattern p = Pattern.compile(EMAILID_REGEX);
			Matcher m = p.matcher(emailId);
			return m.matches();
		}
		return false;
	}

	public static boolean isValidMobileNumber(String number) {
		if (StringUtils.isNoneBlank(number)) {
			Pattern p = Pattern.compile(MOBILENUMBER_REGEX);
			Matcher m = p.matcher(number);
			return m.matches();
		}
		return false;
	}

	public static final String getFormattedDate(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(date);
		}
		return null;
	}
}