package com.shubheksha.service.common;

import org.springframework.stereotype.Service;

import com.shubheksha.utils.Constant;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioService {

	public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("+14159664119");

	@PostConstruct
	public void init() {
		Twilio.init(Constant.ACCOUNT_SID, Constant.AUTH_TOKEN);
	}

	public void sendSms(String toNum, String msg) {

		// Send a text message
		Message message = Message
				.creator(Constant.ACCOUNT_SID, new PhoneNumber(Constant.INDIAN_ISD + toNum), PHONE_NUMBER, msg)
				.create();

		System.out.println("message.getAccountSid(): " + message.getAccountSid());
		System.out.println("message.getApiVersion(): " + message.getApiVersion());
		System.out.println("message.getBody(): " + message.getBody());
		System.out.println("message.getErrorMessage(): " + message.getErrorMessage());
		System.out.println("message.getMessagingServiceSid(): " + message.getMessagingServiceSid());
		System.out.println("message.getNumMedia(): " + message.getNumMedia());
		System.out.println("message.getNumSegments(): " + message.getNumSegments());
		System.out.println("message.getPrice(): " + message.getPrice());
		System.out.println("message.getTo(): " + message.getTo());
		System.out.println("message.getErrorCode(): " + message.getErrorCode());
		System.out.println("message.getDateCreated(): " + message.getDateCreated());
		System.out.println("message.getDateSent(): " + message.getDateSent());
		System.out.println("message.getDateUpdated(): " + message.getDateUpdated());
		System.out.println("message.getDirection(): " + message.getDirection());
		System.out.println("message.getFrom(): " + message.getFrom());
		System.out.println("message.getPriceUnit(): " + message.getPriceUnit());
		System.out.println("message.getStatus(): " + message.getStatus());
		System.out.println("message.getSubresourceUris(): " + message.getSubresourceUris());
		System.out.println("message.getSid(): " + message.getSid());
		System.out.println("message.getUri(): " + message.getUri());
	}
}