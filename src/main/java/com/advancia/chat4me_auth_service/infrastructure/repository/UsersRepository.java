package com.advancia.chat4me_auth_service.infrastructure.repository;

import com.advancia.chat4me_auth_service.infrastructure.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT u FROM UserEntity u")
    List<UserEntity> getUsers();
    Optional<UserEntity> findByUsername(String username);

    @NonNull
    Optional<UserEntity> findById(@NonNull UUID id);
}