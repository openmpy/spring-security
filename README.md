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

---

## 사용자 관리

- UserDetails 인터페이스에 대해
    - 사용자를 표현하는 UserDetails
    - 권한을 표현하는 GrantedAuthority
    - UserDetailsService, UserDetailsManager 를 활용하여 사용자 만들기, 암호 수정 등의 커스텀한 작업을 지원
    - 다양한 유형의 UserDetailsManager (InMemoryUserDetailsManager, JdbcUserDetailsManager 등)

## 사용자 관리를 위한 인터페이스 소개

- UserDetailsService 와 UserDetailsManager 인터페이스를 이용하여 사용자 관리를 수행함 (CRUD 기능)
    - UserDetailsService 는 사용자 이름으로 사용자를 검색함 (Read)
    - UserDetailsManager 는 사용자 추가, 수정, 삭제 작업을 수행함 (Create, Update, Delete)
- 두 인터페이스의 분리는 5대 객체 지향 프로그래밍 원칙(SOLID) 중 I 에 해당하는 Interface Segregation Principle 에 해당함
    - 인터페이스 분리 원칙: 사용하지 않는 인터페이스에 강제로 의존해서는 안됨
- 따라서, Read 와 Write 관련 인터페이스를 분리해두었기 때문에 프레임워크의 유연성이 향상됨
    - 사용자를 인증하는 기능이 필요하면 UserDetailsService 인터페이스만 구현하면 됨
    - 추가, 수정, 삭제 등 더 많은 기능을 제공해야 하면 UserDetailsManager 를 함께 구현하면 됨

## UserDetailsService 소개

- 유저 정보를 조회하는 메서드만 존재
    - username 을 파라미터로 받아 UserDetails 객체를 반환하는 loadUserByUsername 메서드

## UserDetailsManager 소개

- 사용자 생성, 수정, 삭제, 암호 변경, 사용자 존재 여부 확인 메서드를 제공함
    - UserDetailsService 를 extends 하고 있기 때문에 UserDetailsManager 를 구현하면 클라이언트는 UserDetailsService 까지 구현해야 함

## 이용 권리의 집합, GrantedAuthority

- 사용자는 사용자가 수행할 수 있는 작업을 나타내는 이용 권리의 집합을 가짐
    - 보통 권한이라고 표현함
    - 예시: 메뉴 조회 권한, 데이터 수정 권한, 데이터 대량 업로드 권한 등
    - 사용자는 하나 이상의 권한을 가질 수 있으며 GrantedAuthority 라는 인터페이스를 통해 이를 구현할 수 있음

## 사용자 상세 정보, UserDetails

- 스프링 시큐리티 인증 흐름에서 사용자를 나타내는 방법은 필수로 숙지해야 함
    - 사용자에 따라 애플리케이션은 제공할 수 있는 기능을 제한하기도 함
- UserDetails 인터페이스를 기반으로 스프링 시큐리티에서는 사용자를 표현함
    - 인터페이스이기 때문에 애플리케이션 수준에서 UserDetails 를 구현해야 함

--- 

## UserDetailsService 에 대한 이해

- UserDetailsService 는 loadUserByUsername 이라는 하나의 메서드를 제공함
    - 여기서 username 이라는 것은 사용자 ID 와 같은 개념으로 보면 됨
    - 따라서 고유하다고 간주함 (Unique)
    - 반환하는 사용자 정보는 UserDetails
    - 만약 사용자가 존재하지 않으면 UsernameNotFoundException 을 던짐

## UserDetailsManager 구현

- 일반적인 애플리케이션에는 사용자를 관리하는 기능이 필요함
    - 회원가입을 통해 새로운 사용자 추가
    - 내 정보에서 사용자 정보 수정
    - 회원 탈퇴를 위한 사용자 삭제
- 이런 기능을 위해서는 UserDetailsManager 가 필요함
    - 인터페이스로써 구현체가 필요함

---

## PasswordEncoder 인터페이스

- PasswordEncoder 인터페이스를 통해 스프링 시큐리티에서 사용자 암호를 검증하는 방법을 알 수 있음
    - 인증 프로세스에서 암호가 유효한지 확인하는 과정을 거침
    - 암호에 대한 인코딩을 수행할 수 있음
- 인터페이스에는 두 개의 메서드와 하나의 default 메서드가 정의되어 있음
    - encode
        - 주어진 암호의 해시를 제공
        - 암호화를 수행
    - matches
        - 인코딩된 문자열이 원시 암호와 일치한지 검증
    - upgradeEncoding
        - 기본적으로 false 를 반환
        - true 를 반환하도록 메서드를 재정의하면 보안 향상을 위해 인코딩된 암호를 다시 인코딩 함

## DelegatingPasswordEncoder

- 앱 버전이 올라감에 따라 다른 유형의 PasswordEncoder 를 적용해야 한다면?
- 비즈니스 로직에 따라 특정 PasswordEncoder 를 따로 설정해야 한다면?
- 사용자에 따라 다른 PasswordEncoder 를 적용해야 한다면?
- 다양한 방법이 존재하지만 DelegatingPasswordEncoder 가 좋은 선택이 될 수 있음
    - PasswordEncoder 인터페이스에 대한 구현체 중 하나로 자체 인코딩 알고리즘을 구현하는 대신 다른 구현체에 작업을 위임하는 역할을 함