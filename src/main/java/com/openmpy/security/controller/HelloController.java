package com.openmpy.security.controller;

import com.openmpy.security.controller.request.HelloRequestBody;
import com.openmpy.security.service.EncryptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HelloController {

    private final EncryptService encryptService;

    @PostMapping("/api/v1/hello")
    public String hello(@RequestBody HelloRequestBody request) {
        String encrypted = encryptService.encrypt(request.getPassword());
        return "";
    }
}
