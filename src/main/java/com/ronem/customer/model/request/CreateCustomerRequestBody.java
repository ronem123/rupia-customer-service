package com.ronem.customer.model.request;


import com.ronem.customer.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by Ram Mandal on 16/11/2025
 *
 * @System: Apple M1 Pro
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCustomerRequestBody {
    @NotBlank(message = "name should not be empty")
    @Size(min = 5, max = 50, message = "Name should have char between 5 and 50")
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String mobileNumber;

    private String idNumber;

    private String email;

    private String address;
}