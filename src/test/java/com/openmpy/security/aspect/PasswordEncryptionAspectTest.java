package com.openmpy.security.aspect;

import com.openmpy.security.controller.request.HelloRequestBody;
import com.openmpy.security.service.EncryptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncryptionAspectTest {

    PasswordEncryptionAspect aspect;

    @Mock
    EncryptService encryptService;

    @BeforeEach
    void setUp() {
        aspect = new PasswordEncryptionAspect(encryptService);
    }

    @Test
    void test() {
        // given
        HelloRequestBody requestBody = new HelloRequestBody("id", "password");
        when(encryptService.encrypt(any())).thenReturn("encrypted");

        // when
        aspect.fieldEncryption(requestBody);

        // then
        assertThat(requestBody.getPassword()).isEqualTo("encrypted");
    }
}