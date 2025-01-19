package com.shubheksha.serviceImpl;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.shubheksha.service.MailService;
import com.shubheksha.utils.Constant;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

	private static final String MAIL_FORMAT = "text/html";

	private Session session;

	private static final String SMPT_HOST = "smtp.gmail.com";

	private static final String SMPT_PORT = "587";

	private static final String SMPT_AUTH = "true";

	private static final String SMPT_TLS_FLAG = "true";

	@Override
	public boolean sendMail(String[] toemail, String[] ccemail, String subject, String msgContent, String[] fileNames) {
		try {
			Message message = new MimeMessage(session);
			message.setHeader("X-Priority", "3");
			message.setHeader("Precedence", "bulk");
			message.setFrom(new InternetAddress(Constant.SHUBHEKSHA_FROM_EMAIL_ID, "Shubheksha Support"));
			if (toemail != null) {
				for (int i = 0; i < toemail.length; i++) {
					message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toemail[i], false));
				}
			}
			if (ccemail != null) {
				for (int i = 0; i < ccemail.length; i++) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccemail[i], false));
				}
			}
			message.setSubject(subject);

			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msgContent, MAIL_FORMAT);
			multipart.addBodyPart(messageBodyPart);
			File[] files = null;
			if (fileNames != null) {
				files = new File[fileNames.length];
				int counter = 0;
				for (String fileName : fileNames) {
					File file = new File(fileName);
					files[counter++] = file;
					if (file != null && file.exists()) {
						BodyPart attachment = new MimeBodyPart();
						FileDataSource source = new FileDataSource(file);
						attachment.setDataHandler(new DataHandler(source));
						attachment.setFileName(file.getName());
						multipart.addBodyPart(attachment);
					}
				}
			}
			message.setContent(multipart);
			message.setSentDate(new Date());

			log.error("Mail going to mail: {} ", Arrays.toString(toemail));
			Long start = System.currentTimeMillis();
			Transport.send(message);
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
			log.info("Mail going transport " + (System.currentTimeMillis() - start));
			log.error("Mail was sent successfully to: {}", Arrays.toString(toemail));
			return true;
		} catch (Exception e) {
			log.error("Exception in MailService Sending mail to: {}", Arrays.toString(toemail));
			log.info("The Error Message:{}", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@PostConstruct
	public void init() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", SMPT_HOST);
		props.put("mail.smtp.port", SMPT_PORT);
		props.put("mail.smtp.auth", SMPT_AUTH);
		props.put("mail.smtp.starttls.enable", SMPT_TLS_FLAG);
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constant.SHUBHEKSHA_USERNAME, Constant.SHUBHEKSHA_PWD);
			}
		});
		this.session = session;
	}

}
