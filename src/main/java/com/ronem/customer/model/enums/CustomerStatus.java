package com.ronem.customer.model.enums;


/**
 * Created by Ram Mandal on 24/11/2025
 *
 * @System: Apple M1 Pro
 */

public enum CustomerStatus {
    ACTIVE,         //user is in active state and using the app frequently
    INACTIVE,       //user is partially registered. User created but never verified mobile/email
    KYC_PENDING,    //User Kyc pending need to approve from BO
    KYC_REJECTED,   //User KYC is rejected by the BO if data are not justified
    BLOCKED,        //User is blocked due to suspicious behaviour
    DEACTIVATED     //User is temporarily suspended
}
