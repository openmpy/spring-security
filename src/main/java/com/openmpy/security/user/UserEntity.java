package com.openmpy.security.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class UserEntity {

    @Id
    private Long id;

    private String username;
    private String password;
    private String authority;
}