package org.jsp.ebanking.util;

import org.json.JSONObject;
import org.jsp.ebanking.dto.RazorpayDto;
import org.jsp.ebanking.exception.PaymentFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Component
public class PaymentUtil {

	@Value("${razorpay.key}")
	private String key;
	@Value("${razorpay.secret}")
	private String secret;

	public RazorpayDto createOrder(Double amount) {
		try {
			RazorpayClient razorpay = new RazorpayClient(key, secret);
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100);
			orderRequest.put("currency", "INR");
			Order order = razorpay.orders.create(orderRequest);
			return new RazorpayDto(order.get("id"), amount * 100, key, "INR");
		} catch (RazorpayException e) {
			e.printStackTrace();
			throw new PaymentFailedException("Failed to Initialize Payment , We are Working Will fix ASAP");
		}

	}

}
