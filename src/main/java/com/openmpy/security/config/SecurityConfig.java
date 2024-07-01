package com.openmpy.security.config;

import com.openmpy.security.user.InMemoryUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails kim = User.withUsername("suhwan.kim")
                .password("1234")
                .build();
        UserDetails lee = User.withUsername("suhwan.lee")
                .password("2345")
                .build();
        UserDetails park = User.withUsername("suhwan.park")
                .password("3456")
                .build();

        List<UserDetails> users = List.of(kim, lee, park);

        return new InMemoryUserDetailsService(users);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
