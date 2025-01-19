package com.shubheksha.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

	boolean sendMail(String[] toemail, String[] ccemail, String subject, String msgContent, String[] fileNames);

}
