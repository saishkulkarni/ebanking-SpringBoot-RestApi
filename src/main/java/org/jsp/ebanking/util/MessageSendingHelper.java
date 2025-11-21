package org.jsp.ebanking.util;

import java.io.UnsupportedEncodingException;

import org.jsp.ebanking.exception.FailedToSendOtpException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSendingHelper {

	private final JavaMailSender mailSender;

	@Async
	public void sendOtp(String name, String email, int otp) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setTo(email);
			helper.setFrom("ebanking@help.com", "eBanking");
			helper.setSubject("Otp to Create Account at ebanking");

			String htmlMsg = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "<head>\r\n"
					+ "    <meta charset=\"UTF-8\">\r\n" + "    <title>eBanking OTP Verification</title>\r\n"
					+ "    <style>\r\n" + "        body {\r\n"
					+ "            font-family: 'Segoe UI', Arial, sans-serif;\r\n"
					+ "            background-color: #f4f6f8;\r\n" + "            margin: 0;\r\n"
					+ "            padding: 0;\r\n" + "        }\r\n" + "        .container {\r\n"
					+ "            max-width: 500px;\r\n" + "            margin: 40px auto;\r\n"
					+ "            background-color: #ffffff;\r\n" + "            border-radius: 8px;\r\n"
					+ "            box-shadow: 0 2px 8px rgba(0,0,0,0.1);\r\n" + "            padding: 30px;\r\n"
					+ "        }\r\n" + "        .header {\r\n" + "            text-align: center;\r\n"
					+ "            border-bottom: 2px solid #0078d7;\r\n" + "            padding-bottom: 10px;\r\n"
					+ "        }\r\n" + "        .header h2 {\r\n" + "            color: #0078d7;\r\n"
					+ "            margin: 0;\r\n" + "        }\r\n" + "        .content {\r\n"
					+ "            margin-top: 20px;\r\n" + "            color: #333333;\r\n" + "        }\r\n"
					+ "        .otp-box {\r\n" + "            background-color: #f0f8ff;\r\n"
					+ "            border: 1px dashed #0078d7;\r\n" + "            text-align: center;\r\n"
					+ "            font-size: 24px;\r\n" + "            letter-spacing: 3px;\r\n"
					+ "            color: #0078d7;\r\n" + "            padding: 15px;\r\n"
					+ "            margin: 20px 0;\r\n" + "            border-radius: 6px;\r\n" + "        }\r\n"
					+ "        .footer {\r\n" + "            text-align: center;\r\n"
					+ "            font-size: 13px;\r\n" + "            color: #888888;\r\n"
					+ "            margin-top: 30px;\r\n" + "        }\r\n" + "    </style>\r\n" + "</head>\r\n"
					+ "<body>\r\n" + "    <div class=\"container\">\r\n" + "        <div class=\"header\">\r\n"
					+ "            <h2>eBanking Verification</h2>\r\n" + "        </div>\r\n"
					+ "        <div class=\"content\">\r\n" + "            <p>Hello <strong>{{name}}</strong>,</p>\r\n"
					+ "            <p>Your One-Time Password (OTP) for creating an account with <b>eBanking</b> is:</p>\r\n"
					+ "            <div class=\"otp-box\">{{otp}}</div>\r\n"
					+ "            <p>This OTP is valid for the next <b>2 minutes</b>. Please do not share it with anyone.</p>\r\n"
					+ "            <p>Thank you,<br><b>The eBanking Team</b></p>\r\n" + "        </div>\r\n"
					+ "        <div class=\"footer\">\r\n"
					+ "            &copy; 2025 eBanking. All rights reserved.\r\n" + "        </div>\r\n"
					+ "    </div>\r\n" + "</body>\r\n" + "</html>\r\n" + "";

			htmlMsg = htmlMsg.replace("{{name}}", name);
			htmlMsg = htmlMsg.replace("{{otp}}", otp + "");

			helper.setText(htmlMsg, true);

			mailSender.send(message);
			log.info("OTP SENT SUCCESS "+otp);
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("FAILED TO SEND OTP ");
			throw new FailedToSendOtpException("Sorry there is isuue on our side will fix ASAP, Try After Sometime");
		}
	}

	@Async
	public void sendForgotPasswordOtp(String email, int otp) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setTo(email);
			helper.setFrom("ebanking@help.com", "eBanking");
			helper.setSubject("Otp for Reseting the Password");

			String htmlMsg = "<html><body><h1>Your Otp for Reseting Password is {{otp}}</h1></body></html>";

			htmlMsg = htmlMsg.replace("{{otp}}", otp + "");

			helper.setText(htmlMsg, true);

			mailSender.send(message);
			log.info("OTP SENT SUCCESS "+otp);
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("FAILED TO SEND OTP");
			throw new FailedToSendOtpException("Sorry there is isuue on our side will fix ASAP, Try After Sometime");
		}
	}

}
