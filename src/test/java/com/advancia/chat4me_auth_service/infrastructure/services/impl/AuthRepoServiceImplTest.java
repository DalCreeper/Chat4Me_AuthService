package com.advancia.chat4me_auth_service.infrastructure.services.impl;

import com.advancia.chat4me_auth_service.domain.model.AuthToken;
import com.advancia.chat4me_auth_service.domain.model.OTPVerificationRequest;
import com.advancia.chat4me_auth_service.domain.model.User;
import com.advancia.chat4me_auth_service.domain.services.PasswordManager;
import com.advancia.chat4me_auth_service.infrastructure.mappers.AuthEntityMappers;
import com.advancia.chat4me_auth_service.infrastructure.mappers.UserEntityMappers;
import com.advancia.chat4me_auth_service.infrastructure.model.AuthTokenEntity;
import com.advancia.chat4me_auth_service.infrastructure.model.OTPVerificationRequestEntity;
import com.advancia.chat4me_auth_service.infrastructure.model.UserEntity;
import com.advancia.chat4me_auth_service.infrastructure.repository.AuthTokenRepository;
import com.advancia.chat4me_auth_service.infrastructure.repository.OtpVerificationRepository;
import com.advancia.chat4me_auth_service.infrastructure.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthRepoServiceImplTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PasswordManager passwordManager;
    @Mock
    private OtpVerificationRepository otpVerificationRepository;
    @Mock
    private AuthTokenRepository authTokenRepository;
    @Mock
    private AuthEntityMappers authEntityMappers;
    @Mock
    private UserEntityMappers userEntityMappers;
    @InjectMocks
    private AuthRepoServiceImpl authRepoServiceImpl;

    @Test
    void shouldReturnUser_whenFindByUserAndPassIsAllOk() {
        String username = "testUser";
        String password = "testPassword";
        UserEntity userEntity = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username(username)
            .email("testEmail")
            .password(password)
            .build();
        User user = User.builder()
            .id(userEntity.getId())
            .name(userEntity.getName())
            .surname(userEntity.getSurname())
            .username(userEntity.getUsername())
            .email(userEntity.getEmail())
            .password(userEntity.getPassword())
            .build();

        doReturn(Optional.of(userEntity)).when(usersRepository).findByUsername(username);
        doReturn(true).when(passwordManager).matches(password, userEntity.getPassword());
        doReturn(user).when(userEntityMappers).convertFromInfrastructure(userEntity);

        Optional<User> userResult = authRepoServiceImpl.findByUsernameAndPassword(username, password);
        assertTrue(userResult.isPresent());
        assertEquals(Optional.of(user), userResult);

        verify(usersRepository).findByUsername(username);
        verify(userEntityMappers).convertFromInfrastructure(userEntity);
    }

    @Test
    void shouldPropagateException_whenFindByUserAndPassUserRepositoryFails() {
        String username = "testUser";
        String password = "testPassword";

        RuntimeException runtimeException = new RuntimeException("Repository error");

        doThrow(runtimeException).when(usersRepository).findByUsername(username);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.findByUsernameAndPassword(username, password));
        assertSame(runtimeException, ex);

        verify(usersRepository).findByUsername(username);
        verify(userEntityMappers, never()).convertFromInfrastructure(any(UserEntity.class));
    }

    @Test
    void shouldPropagateException_whenUserPasswordNotMatch() {
        String username = "testUser";
        String password = "testPassword";
        UserEntity userEntity = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username(username)
            .email("testEmail")
            .password(password)
            .build();

        RuntimeException runtimeException = new RuntimeException("Wrong password");

        doReturn(Optional.of(userEntity)).when(usersRepository).findByUsername(username);
        doThrow(runtimeException).when(passwordManager).matches(password, userEntity.getPassword());

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.findByUsernameAndPassword(username, password));
        assertSame(runtimeException, ex);

        verify(usersRepository).findByUsername(username);
        verify(userEntityMappers, never()).convertFromInfrastructure(userEntity);
    }

    @Test
    void shouldReturnUser_whenFindByIdIsAllOk() {
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();
        User user = User.builder()
            .id(userEntity.getId())
            .name(userEntity.getName())
            .surname(userEntity.getSurname())
            .username(userEntity.getUsername())
            .email(userEntity.getEmail())
            .password(userEntity.getPassword())
            .build();

        doReturn(Optional.of(userEntity)).when(usersRepository).findById(uuid);
        doReturn(user).when(userEntityMappers).convertFromInfrastructure(userEntity);

        Optional<User> userResult = authRepoServiceImpl.findById(uuid);
        assertTrue(userResult.isPresent());
        assertEquals(Optional.of(user), userResult);

        verify(usersRepository).findById(uuid);
        verify(userEntityMappers).convertFromInfrastructure(userEntity);
    }

    @Test
    void shouldPropagateException_whenFindByIDUserRepositoryFails() {
        UUID uuid = UUID.randomUUID();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doThrow(runtimeException).when(usersRepository).findById(uuid);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.findById(uuid));
        assertSame(runtimeException, ex);

        verify(usersRepository).findById(uuid);
        verify(userEntityMappers, never()).convertFromInfrastructure(any(UserEntity.class));
    }

    @Test
    void shouldReturnUser_whenSaveUserIsAllOk() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();
        UserEntity userEntity = UserEntity.builder()
            .id(user.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();

        doReturn(userEntity).when(userEntityMappers).convertToInfrastructure(user);
        doReturn(userEntity).when(usersRepository).save(userEntity);
        doReturn(user).when(userEntityMappers).convertFromInfrastructure(userEntity);

        User userResult = authRepoServiceImpl.saveUser(user);
        assertEquals(user, userResult);

        verify(userEntityMappers).convertToInfrastructure(user);
        verify(usersRepository).save(userEntity);
        verify(userEntityMappers).convertFromInfrastructure(userEntity);
    }

    @Test
    void shouldPropagateException_whenSaveUserUserRepositoryFails() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .name("testName")
            .surname("testSurname")
            .username("testUser")
            .email("testEmail")
            .password("testPassword")
            .build();
        UserEntity userEntity = UserEntity.builder()
            .id(user.getId())
            .name(user.getName())
            .surname(user.getSurname())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doReturn(userEntity).when(userEntityMappers).convertToInfrastructure(user);
        doThrow(runtimeException).when(usersRepository).save(userEntity);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.saveUser(user));
        assertSame(runtimeException, ex);

        verify(userEntityMappers).convertToInfrastructure(user);
        verify(usersRepository).save(userEntity);
        verify(userEntityMappers, never()).convertFromInfrastructure(any(UserEntity.class));
    }

    @Test
    void shouldSaveCorrectlyOTPVerificationRequest_whenIsAllOk() {
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(Instant.now().plusSeconds(300).getEpochSecond())
            .userId(UUID.randomUUID())
            .build();
        OTPVerificationRequestEntity otpVerificationRequestEntity = OTPVerificationRequestEntity.builder()
            .challengeId(otpVerificationRequest.getChallengeId())
            .otp(otpVerificationRequest.getOtp())
            .expiresAt(otpVerificationRequest.getExpiresAt())
            .userId(otpVerificationRequest.getUserId())
            .build();

        doReturn(otpVerificationRequestEntity).when(authEntityMappers).convertToInfrastructure(otpVerificationRequest);
        doReturn(otpVerificationRequestEntity).when(otpVerificationRepository).save(otpVerificationRequestEntity);

        assertDoesNotThrow(() -> authRepoServiceImpl.saveOTPVerificationRequest(otpVerificationRequest));

        verify(authEntityMappers).convertToInfrastructure(otpVerificationRequest);
        verify(otpVerificationRepository).save(otpVerificationRequestEntity);
    }

    @Test
    void shouldPropagateException_whenSaveOTPVerificationRepositoryFails() {
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(Instant.now().plusSeconds(300).getEpochSecond())
            .userId(UUID.randomUUID())
            .build();
        OTPVerificationRequestEntity otpVerificationRequestEntity = OTPVerificationRequestEntity.builder()
            .challengeId(otpVerificationRequest.getChallengeId())
            .otp(otpVerificationRequest.getOtp())
            .expiresAt(otpVerificationRequest.getExpiresAt())
            .userId(otpVerificationRequest.getUserId())
            .build();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doReturn(otpVerificationRequestEntity).when(authEntityMappers).convertToInfrastructure(otpVerificationRequest);
        doThrow(runtimeException).when(otpVerificationRepository).save(otpVerificationRequestEntity);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.saveOTPVerificationRequest(otpVerificationRequest));
        assertSame(runtimeException, ex);

        verify(authEntityMappers).convertToInfrastructure(otpVerificationRequest);
        verify(otpVerificationRepository).save(otpVerificationRequestEntity);
    }

    @Test
    void shouldReturnOTPVerification_whenIsAllOk() {
        UUID uuid = UUID.randomUUID();
        OTPVerificationRequestEntity otpVerificationRequestEntity = OTPVerificationRequestEntity.builder()
            .challengeId(UUID.randomUUID())
            .otp("123456")
            .expiresAt(Instant.now().plusSeconds(300).getEpochSecond())
            .userId(UUID.randomUUID())
            .build();
        OTPVerificationRequest otpVerificationRequest = OTPVerificationRequest.builder()
            .challengeId(otpVerificationRequestEntity.getChallengeId())
            .otp(otpVerificationRequestEntity.getOtp())
            .expiresAt(otpVerificationRequestEntity.getExpiresAt())
            .userId(otpVerificationRequestEntity.getUserId())
            .build();

        doReturn(Optional.of(otpVerificationRequestEntity)).when(otpVerificationRepository).findById(uuid);
        doReturn(otpVerificationRequest).when(authEntityMappers).convertFromInfrastructure(otpVerificationRequestEntity);

        Optional<OTPVerificationRequest> otpVerificationRequestResult = authRepoServiceImpl.findOTPById(uuid);
        assertTrue(otpVerificationRequestResult.isPresent());
        assertEquals(Optional.of(otpVerificationRequest), otpVerificationRequestResult);

        verify(otpVerificationRepository).findById(uuid);
        verify(authEntityMappers).convertFromInfrastructure(otpVerificationRequestEntity);
    }

    @Test
    void shouldPropagateException_whenFindByIDOTPVerificationRepositoryFails() {
        UUID uuid = UUID.randomUUID();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doThrow(runtimeException).when(otpVerificationRepository).findById(uuid);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.findOTPById(uuid));
        assertSame(runtimeException, ex);

        verify(otpVerificationRepository).findById(uuid);
        verify(authEntityMappers, never()).convertFromInfrastructure(any(OTPVerificationRequestEntity.class));
    }

    @Test
    void shouldSaveCorrectlyAuthToken_whenIsAllOk() {
        AuthToken authToken = AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(UUID.randomUUID())
            .build();
        AuthTokenEntity authTokenEntity = AuthTokenEntity.builder()
            .tokenId(authToken.getTokenId())
            .expiresIn(authToken.getExpiresIn())
            .message(authToken.getMessage())
            .userId(authToken.getUserId())
            .build();

        doReturn(authTokenEntity).when(authEntityMappers).convertToInfrastructure(authToken);
        doReturn(authTokenEntity).when(authTokenRepository).save(authTokenEntity);

        assertDoesNotThrow(() -> authRepoServiceImpl.saveAuthToken(authToken));

        verify(authEntityMappers).convertToInfrastructure(authToken);
        verify(authTokenRepository).save(authTokenEntity);
    }

    @Test
    void shouldPropagateException_whenSaveAuthTokenRepositoryFails() {
        AuthToken authToken = AuthToken.builder()
            .tokenId(UUID.randomUUID())
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(UUID.randomUUID())
            .build();
        AuthTokenEntity authTokenEntity = AuthTokenEntity.builder()
            .tokenId(authToken.getTokenId())
            .expiresIn(authToken.getExpiresIn())
            .message(authToken.getMessage())
            .userId(authToken.getUserId())
            .build();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doReturn(authTokenEntity).when(authEntityMappers).convertToInfrastructure(authToken);
        doThrow(runtimeException).when(authTokenRepository).save(authTokenEntity);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.saveAuthToken(authToken));
        assertSame(runtimeException, ex);

        verify(authEntityMappers).convertToInfrastructure(authToken);
        verify(authTokenRepository).save(authTokenEntity);
    }

    @Test
    void shouldReturnAuthToken_whenFindByIdIsAllOk() {
        UUID uuid = UUID.randomUUID();
        AuthTokenEntity authTokenEntity = AuthTokenEntity.builder()
            .tokenId(UUID.randomUUID())
            .expiresIn(86400000L) // 1 day
            .message("Auth token generated")
            .userId(uuid)
            .build();
        AuthToken authToken = AuthToken.builder()
            .tokenId(authTokenEntity.getTokenId())
            .expiresIn(authTokenEntity.getExpiresIn())
            .message(authTokenEntity.getMessage())
            .userId(authTokenEntity.getUserId())
            .build();

        doReturn(Optional.of(authTokenEntity)).when(authTokenRepository).findById(uuid);
        doReturn(authToken).when(authEntityMappers).convertFromInfrastructure(authTokenEntity);

        Optional<AuthToken> authTokenResult = authRepoServiceImpl.findAuthById(uuid);
        assertTrue(authTokenResult.isPresent());
        assertEquals(Optional.of(authToken), authTokenResult);

        verify(authTokenRepository).findById(uuid);
        verify(authEntityMappers).convertFromInfrastructure(authTokenEntity);
    }

    @Test
    void shouldPropagateException_whenFindByIDAuthTokenRepositoryFails() {
        UUID uuid = UUID.randomUUID();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doThrow(runtimeException).when(authTokenRepository).findById(uuid);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> authRepoServiceImpl.findAuthById(uuid));
        assertSame(runtimeException, ex);

        verify(authTokenRepository).findById(uuid);
        verify(authEntityMappers, never()).convertFromInfrastructure(any(AuthTokenEntity.class));
    }
}