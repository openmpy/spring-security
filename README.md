## UserDetailsService 구성 요소 재정의

- InMemoryUserDetailsManager 구현체를 이용한 기본 구성을 재정의 하는 방법
    - 스프링 빈으로 등록할 수 있음

1. config 패키지를 생성하고 설정 클래스(SecurityConfig)를 생성
2. @Configuration 어노테이션으로 클래스를 구성 클래스로 구분
3. @Bean 어노테이션으로 반환되는 값을 스프링 컨텍스트에 반영
4. InMemoryUserDetailsManager 를 UserDetailsService 로 반영

## 더이상 노출되지 않는 비밀번호

> 별도의 설정이 없을 때는 스프링 시큐리티 기본 비밀번호로 사용할 수 있는 UUID 가 노출되었음

- 왜 비밀번호가 노출되지 않을까?
    - 기본으로 구성된 UserDetailsService 구현체 대신 스프링 컨텍스트에 반영된 UserDetailsService, 즉 InMemoryUserDetailsManager 를 이용함
    - 하지만 사용자와 PasswordEncoder 를 적용하지 않았기 때문에 REST 엔드포인트에 접근이 불가능함
    - 포스트맨으로 호출을 시도 해보면 401 Unauthorized 에러가 발생

## 테스트 사용자 등록

- UserDetailsService 코드가 수정됨
    - UserDetails 추가
    - InMemoryUserDetailsManager.createUser 호출
- 참고) User 클래스는 스프링 시큐리티에서 제공하는 객체로 사용자를 나타냄

## PasswordEncoder 재정의

- PasswordEncoder 도 다시 정의를 해야 함
    - 기본 UserDetailsService 를 이용하면 자체적으로 기본 PasswordEncoder 를 설정해줌
    - 하지만 커스텀하게 UserDetailsService 를 설정하게 되면 PasswordEncoder 역시 직접 재설정 해줘야 함
- 따라서 UserDetailsService 를 스프링 빈으로 재정의 했던 것처럼 PasswordEncoder 도 별도의 스프링 빈으로 재정의 하면 해결할 수 있음

## NoOpPasswordEncoder 란?

> 비밀번호에 암호화를 적용하지 않고 평문 그대로 사용하는 PasswordEncoder

1. deprecated 되었기 때문에 개발 환경에서만 사용하고 운영 환경에서는 절대로 사용하지 말기
2. PasswordEncoder 인터페이스를 상속받아 구현함
3. 인코딩 알고리즘이 비밀번호를 그대로 반환함
4. 비밀번호를 비교하는 로직은 단순 문자열 .equals()

## 엔드 포인트에 따른 접근 권한 부여 재정의

- 엔드 포인트에 따라 접근하도록 허용하거나 제한할 수 있음
    - SecurityFilterChain, WebSecurityCustomizer 에 대한 스프링 빈을 재정의 할 수 있음
    - SecurityConfig 설정 클래스 내에 설정하면 됨
- auth.anyRequest().authenticated()
    - 모든 요청에 대한 인증을 요구
- httpBasic(withDefaults())
    - HTTP Basic 인증
- permitAll() 으로 변경하면 별도의 자격 증명 없이 REST 엔드포인트를 호출할 수 있음
    - ID, PW 설정 없이도 "Hello World!" 응답을 받음

## AuthenticationProvider 구현 재정의

- AuthenticationProvider 는 인증 공급자에 해당하는 부분으로 인증 논리를 구현하고 사용자 관리와 비밀번호 관리를 각각 UserDetailsService 와 PasswordEncoder 에 위임함

## CustomAuthenticationProvider

- AuthenticationProvider 를 상속받아 구현함
    - CustomAuthenticationProvider 는 @Component 어노테이션으로 스프링 빈으로 등록함
    - config 패키지 아래에 생성
    - 인증 로직을 구현하기 위해 if 문을 추가 (id 값이 "suhwan" 이고 pw 값이 "1234" 일 때)