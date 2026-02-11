/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:16
 */


package com.ronem.customer.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuthClient authClient;

    @Override
    public CustomerResponse registerNewCustomer(CreateCustomerRequestBody request) {
        CreateUserRequest userRequest = customerMapper.toUserRequest(request);
        userRequest.setUserRole("CUSTOMER");
        ApiResponse<CreateUserResponse> response = authClient.createUser(userRequest);
        if (!response.isSuccess()) {
            throw new RuntimeException("User creation failed with " + response.getMessage());
        }
        //if user is created successfully in rupia-auth-service, retrieve userId to store in the customer table
        Long userId = response.getData().userId();
        Customer newCustomer = customerMapper.toEntity(request);
        newCustomer.setUserId(userId);
        newCustomer.setStatus(CustomerStatus.KYC_PENDING);
        newCustomer.setCreatedAt(LocalDateTime.now());

        return customerMapper.toResponse(customerRepository.save(newCustomer));
    }

}