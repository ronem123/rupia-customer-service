/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:17
 */


package com.ronem.customer.model.response;

import com.ronem.customer.model.enums.CustomerStatus;
import com.ronem.customer.model.enums.Gender;

import java.time.LocalDate;

public record CustomerResponse(
        Long id,
        String name,
        LocalDate birthDate,
        String contact,
        String idNumber,
        Gender gender,
        String email,
        String address,
        CustomerStatus customerStatus,
        LocalDate createdAt
) {
}