/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:15:37
 */


package com.ronem.customer.service.client;

import com.ronem.customer.model.request.client.CreateUserRequest;
import com.ronem.customer.model.response.ApiResponse;
import com.ronem.customer.model.response.CreateUserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthClient {

    private final WebClient authWebClient;

    public AuthClient(@Qualifier("rupia-auth-service") WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public ApiResponse<CreateUserResponse> createUser(CreateUserRequest requestBody) {
        return authWebClient
                .post()
                .uri("/internal/create-user")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse ->
                                clientResponse
                                        .bodyToMono(String.class)
                                        .map(RuntimeException::new))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<CreateUserResponse>>() {
                })
                .block();
    }

}
