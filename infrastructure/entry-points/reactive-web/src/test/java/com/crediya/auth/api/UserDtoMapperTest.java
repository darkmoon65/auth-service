package com.crediya.auth.api;

import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.api.mapper.UserDtoMapper;
import com.crediya.auth.model.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {

    private final UserDtoMapper mapper = Mappers.getMapper(UserDtoMapper.class);

    @Test
    void toResponse_shouldMapCreateUserDtoToUser() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("Cesar");
        dto.setLastname("Santillana");
        dto.setEmail("cs6@gmail.com");
        dto.setBaseSalary(BigDecimal.valueOf(5000));

        User user = mapper.toResponse(dto);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(dto.getName());
        assertThat(user.getLastname()).isEqualTo(dto.getLastname());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getBaseSalary()).isEqualTo(dto.getBaseSalary());
    }

    @Test
    void toResponseList_shouldMapListCorrectly() {
        User user1 = new User();
        user1.setName("Cesar");
        User user2 = new User();
        user2.setName("Ana");

        List<User> mapped = mapper.toResponseList(List.of(user1, user2));

        assertThat(mapped).hasSize(2);
        assertThat(mapped).extracting(User::getName).containsExactly("Cesar", "Ana");
    }

    @Test
    void toResponse_shouldReturnNullWhenInputIsNull() {
        User user = mapper.toResponse(null);
        assertThat(user).isNull();
    }

    @Test
    void toResponseList_shouldReturnNullWhenInputIsNull() {
        List<User> mapped = mapper.toResponseList(null);
        assertThat(mapped).isNull();
    }

    @Test
    void toResponseList_shouldReturnEmptyListWhenInputIsEmpty() {
        List<User> mapped = mapper.toResponseList(List.of());
        assertThat(mapped).isEmpty();
    }
}
