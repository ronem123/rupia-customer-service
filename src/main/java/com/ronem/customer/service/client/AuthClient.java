/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:15:37
 */


package com.ronem.customer.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronem.customer.exception.AuthServiceException;
import com.ronem.customer.model.request.client.CreateUserRequest;
import com.ronem.customer.model.response.ApiErrorResponse;
import com.ronem.customer.model.response.ApiResponse;
import com.ronem.customer.model.response.CreateUserResponse;
import com.ronem.rupiasecuritylib.constants.HeaderUtil;
import com.ronem.rupiasecuritylib.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthClient {

    private final WebClient authWebClient;
    private final JwtProperties jwtProperties;

    public AuthClient(@Qualifier("rupia-auth-service") WebClient authWebClient,
                      JwtProperties jwtProperties) {
        this.authWebClient = authWebClient;
        this.jwtProperties = jwtProperties;
    }

//    public ApiResponse<CreateUserResponse> createUser(CreateUserRequest requestBody) {
//        log.info("AuthClient : createUser() called with : {}", requestBody);
//        log.info("AuthClient : Secret {}", jwtProperties.getAccessSecret());
//
//        return authWebClient
//                .post()
//                .uri("/auth/internal/users")
//                .header(HeaderUtil.xInternalSecret, jwtProperties.getAccessSecret())
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .exchangeToMono(clientResponse -> {
//                    log.info("========================================");
//                    log.info("AUTH CLIENT RESPONSE STATUS: {}", clientResponse.statusCode());
//                    log.info("AUTH CLIENT RESPONSE HEADERS: {}", clientResponse.headers().asHttpHeaders());
//
//                    return clientResponse.bodyToMono(String.class)
//                            .defaultIfEmpty("<<EMPTY BODY>>")
//                            .map(rawBody -> {
//                                log.info("AUTH CLIENT RAW RESPONSE BODY: {}", rawBody);
//                                log.info("========================================");
//
//                                if (clientResponse.statusCode().isError()) {
//                                    throw new AuthServiceException(
//                                            HttpStatus.valueOf(clientResponse.statusCode().value()),
//                                            rawBody
//                                    );
//                                }
//
//                                try {
//                                    ObjectMapper objectMapper = new ObjectMapper();
//                                    return objectMapper.readValue(
//                                            rawBody,
//                                            new TypeReference<ApiResponse<CreateUserResponse>>() {}
//                                    );
//                                } catch (Exception e) {
//                                    throw new RuntimeException("Failed to parse auth response: " + rawBody, e);
//                                }
//                            });
//                })
//                .block();
//    }


    public ApiResponse<CreateUserResponse> createUser(CreateUserRequest requestBody) {
        log.info("AuthClient : createUser() called with :{}", requestBody);
        log.info("AuthClient : Secret {}", jwtProperties.getAccessSecret());
        return authWebClient
                .post()
                .uri("/auth/internal/users")
                .header(HeaderUtil.xInternalSecret, jwtProperties.getAccessSecret())
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error status: {}", clientResponse.statusCode());
                    return clientResponse
                            .bodyToMono(ApiErrorResponse.class)
                            .flatMap(error -> {
                                log.error("Auth error response: {}", error);
                                return Mono.error(new AuthServiceException(
                                        HttpStatus.valueOf(clientResponse.statusCode().value()),
                                        error.message()
                                ));
                            });
                })
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CreateUserResponse>>() {
                })
                .doOnNext(body -> {
                    log.info("========================================");
                    log.info("Response received from auth service");
                    log.info("Body: {}", body);
                    if (body != null) {
                        log.info("Success: {}", body.isSuccess());
                        log.info("Message: {}", body.getMessage());
                        log.info("Data: {}", body.getData());
                    }
                    log.info("========================================");
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Auth service returned empty body");
                    return Mono.error(new RuntimeException("Empty Response from auth service"));
                }))
                .block();
    }

    public void deleteUser(Long userId) {
        authWebClient
                .delete()
                .uri("/auth/internal/users/" + userId)
                .retrieve()
                .toBodilessEntity().block();
    }
}
