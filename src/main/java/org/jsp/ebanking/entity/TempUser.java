package org.jsp.ebanking.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@RedisHash(value = "TempUser", timeToLive = 180)
public class TempUser {
	private String name;
	@Id
	private String email;
	private String mobile;
	private String dob;
	private String password;
	private String role;
	private int otp;
}
