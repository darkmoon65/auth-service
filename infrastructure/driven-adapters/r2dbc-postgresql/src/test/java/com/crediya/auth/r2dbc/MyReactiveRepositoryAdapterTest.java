package com.crediya.auth.r2dbc;

import com.crediya.auth.model.user.User;
import com.crediya.auth.r2dbc.entity.UserEntity;
import com.crediya.auth.r2dbc.utils.UserTestSyntheticData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    private TransactionalOperator transactionalOperator;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueByEmail() {
        UserEntity entityUser = UserTestSyntheticData.buildEntity();
        User modelUser = UserTestSyntheticData.buildModel();

        when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(modelUser));
        Mono<User> result = repositoryAdapter.getUserByEmail("test@email.com");

        StepVerifier.create(result)
                .expectNext(modelUser)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity entityUser = UserTestSyntheticData.buildEntity();
        User modelUser = UserTestSyntheticData.buildModel();

        when(repository.findAll()).thenReturn(Flux.just(entityUser));
        when(mapper.map(entityUser, User.class)).thenReturn(modelUser);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value ->
                        value.getId().equals("1") &&
                                value.getName().equals("Juan") &&
                                value.getLastname().equals("Pérez") &&
                                value.getBaseSalary().compareTo(BigDecimal.valueOf(2500.00)) == 0
                )
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        UserEntity entityUser = UserTestSyntheticData.buildEntity();
        User modelUser = UserTestSyntheticData.buildModel();

        when(transactionalOperator.execute(any())).thenReturn(Flux.just(modelUser));

        Mono<User> result = repositoryAdapter.saveUser(modelUser);

        StepVerifier.create(result)
                .expectNext(modelUser)
                .verifyComplete();
    }
}
