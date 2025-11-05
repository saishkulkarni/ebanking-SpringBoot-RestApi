package org.jsp.ebanking.service;

import java.time.Duration;

import org.jsp.ebanking.dto.UserDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;

	public void saveUserDto(UserDto dto) {
		redisTemplate.opsForValue().set("dto:" + dto.getEmail(), dto, Duration.ofMinutes(15));
	}

	public void saveUserOtp(String email, int otp) {
		redisTemplate.opsForValue().set("otp:" + email, otp, Duration.ofMinutes(2));
	}

	public int fetchOtp(String email) {
		Object otp = redisTemplate.opsForValue().get("otp:" + email);
		if (otp != null)
			return (int) otp;
		else
			return 0;
	}

	public UserDto fetchUserDto(String email) {
		return (UserDto) redisTemplate.opsForValue().get("dto:" + email);
	}

	public void deleteUserDto(String email) {
		redisTemplate.delete("dto:" + email);
	}

	public void deleteUserOtp(String email) {
		redisTemplate.delete("otp:" + email);
	}

}
