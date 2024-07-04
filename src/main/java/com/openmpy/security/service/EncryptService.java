package com.openmpy.security.service;

import org.springframework.stereotype.Service;

@Service
public class EncryptService {

    public String encrypt(String before) {
        return "encrypted_" + before;
    }
}
