package com.ronem.customer.controller;

import com.ronem.customer.model.request.CreateCustomerRequestBody;
import com.ronem.customer.model.response.ApiResponse;
import com.ronem.customer.model.response.CustomerResponse;
import com.ronem.customer.service.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;


    @GetMapping(value = "/greet")
    ResponseEntity<HashMap<String, String>> greet() {
        HashMap<String, String> body = new HashMap<>();
        body.put("Status", "success");
        body.put("Message", "Welcome to microservice");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<CustomerResponse>> createNewUser(@RequestBody CreateCustomerRequestBody request) {
        CustomerResponse response = customerService.registerNewCustomer(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
    }

    /**
     * PUT mapping to verify customer's EKYC and update customer EKYC status from EKYC_PENDING to ACTIVE
     *
     * @param userId
     * @return [boolean]
     */
    @PutMapping("/internal/customers/{customerId}/ekyc/verify")
    ResponseEntity<ApiResponse<Boolean>> verifyCustomerEKYC(@PathVariable Long userId) {
        boolean verified = customerService.verifyCustomerEKYC(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "success", verified));
    }

}
