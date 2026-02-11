/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:15:19
 */


package com.ronem.customer.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Bean for AuthService client, will be used form communication with rupia-auth-service
     *
     * @param baseUrl
     * @return
     */
    @Bean("rupia-auth-service")
    public WebClient authServiceWebClient(
            //get base url from application.dev|docker.yaml
            @Value("${services.rupia-auth-service.base-url}") String baseUrl,
            @LoadBalanced WebClient.Builder builder
    ) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)   // connection timeout
                .responseTimeout(Duration.ofSeconds(5));              // response timeout

        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Bean for WalletService client, will be used form communication with rupia-wallet-service
     *
     * @param baseUrl
     * @return
     */
    @Bean("rupia-wallet-service")
    public WebClient walletServiceWebClient(
            @Value("${services.rupia-wallet-service.base-url}") String baseUrl
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}