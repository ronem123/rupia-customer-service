package com.ronem.customer.model.entity;

import com.ronem.customer.model.enums.CustomerStatus;
import com.ronem.customer.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Ram Mandal on 16/11/2025
 *
 * @System: Apple M1 Pro
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer_table",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_mobile_id_no",
                        columnNames = {"customer_mob_no", "customer_id_number"}
                )},

        //indexes help during filter and search
        indexes = {
                @Index(name = "idx_customer_id_number", columnList = "customer_id_number"),
                @Index(name = "idx_customer_dob", columnList = "customer_date_of_birth")
        })
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "customer_full_name", nullable = false, length = 100)
    private String name;

    @Column(name = "customer_date_of_birth", nullable = false, length = 10)
    private LocalDate birthDate;

    @Column(name = "customer_mob_no", unique = true, nullable = false, length = 15)
    private String contact;

    @Column(name = "customer_id_number", unique = true, nullable = false)
    private String idNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_gender", nullable = false, length = 10)
    private Gender gender;

    @Column(name = "customer_email", unique = true, nullable = false)
    private String email;

    @Column(name = "customer_address", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status", nullable = false, length = 15)
    private CustomerStatus status;

    //create and store the time of the customer's first creation; this won't be updatable once set
    @CreationTimestamp
    @Column(name = "customer_created_at", updatable = false)
    private LocalDateTime createdAt;

}
