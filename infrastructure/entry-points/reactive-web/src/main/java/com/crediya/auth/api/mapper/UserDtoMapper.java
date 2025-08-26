package com.crediya.auth.api.mapper;


import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.api.dto.UserDto;
import com.crediya.auth.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoMapper {

    User toResponse(CreateUserDto dto);
    
    List<User> toResponseList(List<User> users);

}
