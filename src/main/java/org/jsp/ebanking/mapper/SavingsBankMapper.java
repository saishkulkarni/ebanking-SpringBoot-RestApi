package org.jsp.ebanking.mapper;

import org.jsp.ebanking.dto.SavingAccountDto;
import org.jsp.ebanking.entity.SavingBankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SavingsBankMapper {

	@Mapping(target = "aadharNumber", source = "aadhar")
	@Mapping(target = "panNumber", source = "pan")
	@Mapping(target = "ifscCode", expression = "java(\"EBNK000001\")")
	@Mapping(target = "branch", expression = "java(\"EBANK-DEFAULT\")")
	@Mapping(target = "balance", expression = "java(0.0)")
	SavingBankAccount toEntity(SavingAccountDto dto);

}
