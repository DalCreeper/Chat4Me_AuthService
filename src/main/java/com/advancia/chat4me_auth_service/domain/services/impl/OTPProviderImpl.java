package com.advancia.chat4me_auth_service.domain.services.impl;

import com.advancia.chat4me_auth_service.domain.services.OTPProvider;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OTPProviderImpl implements OTPProvider {
    @Override
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}