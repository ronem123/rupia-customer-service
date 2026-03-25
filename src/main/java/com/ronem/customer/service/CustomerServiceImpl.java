/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:16
 */


package com.ronem.customer.service;

import com.ronem.customer.exception.AuthServiceException;
import com.ronem.customer.exception.BadRequestException;
import com.ronem.customer.mapper.CustomerMapper;
import com.ronem.customer.model.entity.Customer;
import com.ronem.customer.model.enums.CustomerStatus;
import com.ronem.customer.model.request.CreateCustomerRequestBody;
import com.ronem.customer.model.request.client.CreateUserRequest;
import com.ronem.customer.model.response.ApiResponse;
import com.ronem.customer.model.response.CreateUserResponse;
import com.ronem.customer.model.response.CustomerResponse;
import com.ronem.customer.repository.CustomerRepository;
import com.ronem.customer.service.client.AuthClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuthClient authClient;

    @Override
    public CustomerResponse registerNewCustomer(CreateCustomerRequestBody request) {
        log.info("Register new customer from CustomerService");
        Long userId = -1L;
        try {
            CreateUserRequest userRequest = customerMapper.toUserRequest(request);
            userRequest.setUserRole("CUSTOMER");
            log.info("Setting user Role as {}", userRequest.getUserRole());
            ApiResponse<CreateUserResponse> response = authClient.createUser(userRequest);
            log.info("User create Response {}", response);
            if (response == null || !response.isSuccess()) {
                log.error("Response was null");
                throw new RuntimeException("User creation failed with " + response.getMessage());
            }
            //if user is created successfully in rupia-auth-service, retrieve userId to store in the customer table
            userId = response.getData().userId();
            Customer newCustomer = customerMapper.toEntity(request);
            newCustomer.setUserId(userId);
            newCustomer.setStatus(CustomerStatus.KYC_PENDING);
            newCustomer.setCreatedAt(LocalDateTime.now());
            return customerMapper.toResponse(customerRepository.save(newCustomer));

        } catch (Exception exception) {
            if (userId != -1) {
                //delete from auth-service
                authClient.deleteUser(userId);
            }
            throw exception;
        }
    }

    @Transactional
    @Override
    public Boolean verifyCustomerEKYC(Long customerId) {
        Customer customer = customerRepository.getCustomerByUserId(customerId)
                .orElseThrow(() -> new AuthServiceException(HttpStatus.NOT_FOUND, "Sorry but user not exists"));
        if (customer.getStatus() == CustomerStatus.ACTIVE) {
            throw new BadRequestException("User already verified");
        }
        customer.setStatus(CustomerStatus.ACTIVE);
        return true;
    }

}