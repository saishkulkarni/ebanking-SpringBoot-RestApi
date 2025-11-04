package org.jsp.ebanking.service;

import org.jsp.ebanking.dto.UserDto;
import org.jsp.ebanking.entity.TempUser;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<TempUser> register(UserDto dto);

	ResponseEntity<TempUser> fetch(String email);

}
