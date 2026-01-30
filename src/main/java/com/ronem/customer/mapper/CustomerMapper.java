/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:27
 */


package com.ronem.customer.mapper;

import com.ronem.customer.model.entity.Customer;
import com.ronem.customer.model.request.CreateCustomerRequestBody;
import com.ronem.customer.model.request.client.CreateUserRequest;
import com.ronem.customer.model.response.CreateUserResponse;
import com.ronem.customer.model.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    Customer toEntity(CreateCustomerRequestBody request);

    //map between CustomerRequest and UserRequest
    CreateUserRequest toUserRequest(CreateCustomerRequestBody requestBody);
}