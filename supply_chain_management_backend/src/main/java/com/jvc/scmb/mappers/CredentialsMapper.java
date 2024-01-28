package com.jvc.scmb.mappers;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	CredentialsDto entityToDto(Credentials credentials);
	
	Credentials requestDtoToEntity(CredentialsDto credentialsDto);

}
