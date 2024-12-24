package com.shubheksha.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

	boolean sendMail(String[] toemail, String[] ccemail, String[] fileNames, String subject, String msgContent,
			ByteArrayOutputStream[] outputStreams);
}
