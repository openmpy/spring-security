package com.openmpy.security.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SuhwanUserTest {

    @Test
    void suhwanUserTest() {
        // given & then
        SuhwanUser suhwan = new SuhwanUser();

        // then
        assertThat(suhwan.getUsername()).isEqualTo("suhwan");
        assertThat(suhwan.getPassword()).isEqualTo("1234");
        assertThat(suhwan.getAuthorities().size()).isEqualTo(1);

        Optional<? extends GrantedAuthority> read = suhwan.getAuthorities()
                .stream()
                .filter(authority -> authority.getAuthority().equals("READ"))
                .findFirst();

        read.ifPresent(each -> assertThat(each.getAuthority()).isEqualTo("READ"));
    }
}