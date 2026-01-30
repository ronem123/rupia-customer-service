/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:13:30
 */


package com.ronem.customer.model.entity;

import com.ronem.customer.model.enums.OtpType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "otp_table")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "otp_reference")
    private String otpRef;

    private String otp;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    @Column(name = "otp_created_at", updatable = false)
    private LocalDateTime otpCreatedAt;

    @Column(name = "otp_expire_at")
    private LocalDateTime otpExpireAt;

    @Column(name = "is_otp_used")
    private boolean isOtpUsed = false;

}