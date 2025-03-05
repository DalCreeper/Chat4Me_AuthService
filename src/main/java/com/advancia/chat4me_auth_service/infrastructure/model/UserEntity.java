package com.advancia.chat4me_auth_service.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String surname;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Column(unique = true)
    private String tokenId;

    public void setPassword(String rawPassword) {
        this.password = new BCryptPasswordEncoder().encode(rawPassword);
    }

    public boolean checkPassword(String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, this.password);
    }
}