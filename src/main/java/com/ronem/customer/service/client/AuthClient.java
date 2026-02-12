/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:15:37
 */


package com.ronem.customer.service.client;

import com.ronem.customer.exception.AuthServiceException;
import com.ronem.customer.model.request.client.CreateUserRequest;
import com.ronem.customer.model.response.ApiErrorResponse;
import com.ronem.customer.model.response.ApiResponse;
import com.ronem.customer.model.response.CreateUserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClient {

    private final WebClient authWebClient;

    public AuthClient(@Qualifier("rupia-auth-service") WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public ApiResponse<CreateUserResponse> createUser(CreateUserRequest requestBody) {
        return authWebClient
                .post()
                .uri("/auth/internal/users")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse ->
                                clientResponse
                                        .bodyToMono(ApiErrorResponse.class)
                                        .flatMap(error ->
                                                Mono.error(
                                                        new AuthServiceException(HttpStatus.valueOf(clientResponse.statusCode().value()), error.message())
                                                )
                                        ))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CreateUserResponse>>() {
                })
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
