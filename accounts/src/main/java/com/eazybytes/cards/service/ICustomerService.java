package com.eazybytes.cards.service;

import com.eazybytes.cards.dto.CustomerDetailsDto;

public interface ICustomerService {
    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer details based on a given mobile number
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
