package com.crediya.auth.r2dbc;

import com.crediya.auth.model.user.dto.LogInDTO;
import com.crediya.auth.model.user.dto.TokenDTO;
import com.crediya.auth.model.user.User;
import com.crediya.auth.model.user.gateways.UserRepository;
import com.crediya.auth.r2dbc.document.CustomUserDocument;
import com.crediya.auth.r2dbc.entity.UserEntity;
import com.crediya.auth.r2dbc.helper.ReactiveAdapterOperations;
import com.crediya.auth.security.exception.LoginValidationException;
import com.crediya.auth.security.exception.TokenValidationException;
import com.crediya.auth.security.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        String,
        UserReactiveRepository
        > implements UserRepository {
    private final TransactionalOperator transactionalOperator;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RoleReactiveRepository roleReactiveRepository;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository,
                                         ObjectMapper mapper,
                                         TransactionalOperator transactionalOperator,
                                         PasswordEncoder passwordEncoder,
                                         JwtProvider jwtProvider,
                                         RoleReactiveRepository roleReactiveRepository) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.transactionalOperator = transactionalOperator;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.roleReactiveRepository = roleReactiveRepository;
    }

    @Override
    public Mono<User> saveUser(User user) {
        log.info("Logger adapter {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return transactionalOperator.execute(status ->
                super.save(user)).single();
    }

    @Override
    public Flux<User> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Mono<TokenDTO> login(LogInDTO logInDto) {
        return repository.findByEmail(logInDto.email())
                .filter(user -> passwordEncoder.matches(logInDto.password(), user.getPassword()))
                .flatMap(user -> roleReactiveRepository.findById(user.getIdRol())
                        .map(role -> new CustomUserDocument(user, role.getName())))
                .map(user -> new TokenDTO(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new LoginValidationException()));
    }
}
