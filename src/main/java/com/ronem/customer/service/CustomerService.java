package com.ronem.customer.service;


import com.ronem.customer.model.request.CreateCustomerRequestBody;
import com.ronem.customer.model.response.CustomerResponse;

/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:08
 */

public interface CustomerService {
    CustomerResponse registerNewCustomer(CreateCustomerRequestBody requestBody);

    Boolean verifyCustomerEKYC(Long customerId);
}
