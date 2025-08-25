package com.crediya.auth.api.mapper;


import com.crediya.auth.api.dto.UserDto;
import com.crediya.auth.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserDto toResponse(User user);

    List<User> toResponseList(List<User> users);

}
