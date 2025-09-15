package com.crediya.auth.api;

import com.crediya.auth.api.config.UserPath;
import com.crediya.auth.api.dto.CreateUserDto;
import com.crediya.auth.api.exception.ErrorResponse;
import com.crediya.auth.model.user.User;
import com.crediya.auth.model.user.dto.LogInDTO;
import com.crediya.auth.model.user.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {
    private final UserPath userPath;

    @Bean
    @RouterOperations(
            {
                    @RouterOperation(
                            path = "/api/v1/users",
                            method = RequestMethod.POST,
                            beanClass = Handler.class,
                            beanMethod = "listenCreateUser",
                            operation = @Operation(
                                    operationId = "listenCreateUser",
                                    summary = "Registrar nuevo usuario",
                                    description = "Recibe objeto CreateUserDto y guarda el usuario",
                                    tags = "Usuarios",
                                    parameters = {
                                            @io.swagger.v3.oas.annotations.Parameter(
                                                    name = "Authorization",
                                                    in = io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER,
                                                    required = true,
                                                    description = "Token JWT, ejemplo: Bearer <token>"
                                            )
                                    },
                                    requestBody = @RequestBody(
                                            required = true,
                                            description = "Datos de usuario a registrar",
                                            content = @Content(schema = @Schema(implementation = CreateUserDto.class))
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "201", description = "Usuario guardado",
                                                    content = @Content(schema = @Schema(implementation = User.class))),
                                            @ApiResponse(responseCode = "400", description = "Error de validaciones",
                                                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                                            @ApiResponse(responseCode = "409", description = "Error email ya existente",
                                                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                                    }

                            )),
                    @RouterOperation(
                            path = "/api/v1/auth/login",
                            method = RequestMethod.POST,
                            beanClass = Handler.class,
                            beanMethod = "logIn",
                            operation = @Operation(
                                    operationId = "loginUser",
                                    summary = "Login de usuario",
                                    description = "Recibe objeto LogInDTO y retorna TokenDTO con JWT",
                                    tags = "Autenticación",
                                    requestBody = @RequestBody(
                                            required = true,
                                            description = "Datos de login",
                                            content = @Content(schema = @Schema(implementation = LogInDTO.class))
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "Login exitoso",
                                                    content = @Content(schema = @Schema(implementation = TokenDTO.class))),
                                            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                                                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                                            @ApiResponse(responseCode = "409", description = "Error en la solicitud",
                                                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                                    }
                            )
                    )
            })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(userPath.getUsers()), handler::listenCreateUser)
                .andRoute(POST("/api/v1/auth/login"), handler::logIn)
                .andRoute(GET("/api/v1/users/{email}"), handler::getUserByEmail);
    }
}
