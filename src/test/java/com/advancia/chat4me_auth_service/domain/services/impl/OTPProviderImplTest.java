package com.advancia.chat4me_auth_service.domain.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class OTPProviderImplTest {
    @Mock
    private Random random;
    @InjectMocks
    private OTPProviderImpl otpProviderImpl;

    @Test
    void shouldReturnOTP_whenIsAllOk() {
        String otpResult = otpProviderImpl.generateOtp();

        assertEquals(6, otpResult.length());

        assertTrue(otpResult.matches("\\d{6}"), "The OTP must consist of numeric digits only.");
    }

    @Test
    void shouldReturnFixedOTP_whenIsAllOk() {
        String fixedOTP = "123456";

        doReturn(23456).when(random).nextInt(900000);

        String generatedOTP = otpProviderImpl.generateOtp();

        assertEquals(fixedOTP, generatedOTP, "The generated OTP must be exactly '123456'");
    }
}