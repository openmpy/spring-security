package com.openmpy.security.controller.request;

import com.openmpy.security.annotation.CustomEncryption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HelloRequestBody {

    private String id;

    @CustomEncryption
    private String password;
}
