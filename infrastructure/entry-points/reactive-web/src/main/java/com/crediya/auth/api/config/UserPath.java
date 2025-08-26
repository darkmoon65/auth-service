package com.crediya.auth.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class UserPath {
    private String Users;
    private String usersById;
}
