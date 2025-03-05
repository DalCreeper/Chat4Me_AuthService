package com.advancia.chat4me_auth_service.infrastructure.repository;

import com.advancia.chat4me_auth_service.infrastructure.model.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, UUID> { }