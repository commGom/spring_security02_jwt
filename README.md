# spring_security02_jwt

## 1. JWT 프로젝트 세팅
    - Lombok
    
    - DevTools
    
    - Spring Web
    
    - Spring Data JPA
    
    - MySQL Driver
    
    - Spring Security
    
    - java-jwt : JWT를 만들어 주는 라이브러리

## 2. yml 파일 세팅 및 실행 테스트

## 3. security 설정
    - User Entity 만들기
    
    - SecurityConfig 설정 (stateless로 세션 사용하지 않겠다)
    
    - CorsConfig 설정 (SpringConfig filter 설정) : 모든 CrossOrigin 정책 벗어날 수 있다.  
      @CrossOrigin의 경우 (인증이 없을 때 사용할 수 있다)

### 확인 인증이 필요없는 url의 경우 session이 없기 때문에 바로 실행된다.

### 권한이 필요한 것은 아직 실행되지않음.(403 error)

## 4. filter 만들기
    - 1. filter 만들기
    
    - 2. SecurityConfig에서 filter 설정 (addFilterBefore(), addFilterAfter()) → 굳이 SecurityConfig에 Filter를 해줄 필요 없다.
    
    - 3. FilterConfig 생성하여 Filter 적용할 수 있다.

- SecurityConfig에 추가한 http. addFilterBefore(), addFilterAfter()가 먼저 실행된다.

## 5. jwt 임시 토큰 테스트
  - 1. downcasting해서 하기
  - 2. 요청의 헤더에서 Authorization 값 받아오기
  - 3. 요청 방식이 post일 때만 동작 시키기위한 조건문 작성 
     ```
     테스트했을 때는 모든 요청에 대해 filterChain을 이용하여 doFilter 동작되나, 
     앞으로는 원하는 토큰이 Authroization(서버에서 정해놓은 값)이 왔을 때에만 dofilter동작하도록 설정하여 처리할 것이다.
     ```
- 4. 약식으로 원하는 로직 처리하
- 5. Security가 시작되기전에 filter를 동작하게 하여 Header의 Authorization에 Token을 가지고 있는지 처리하기도록 하기
    
## 6. 로그인 진행
### 1.  토큰 : id,pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
### 2. 요청할 때 마다 header에 Authorization에 value값으로 토큰을 가져오면, 이 토큰이 서버에서 만든 토큰인지 검증만 하면 된다. (RSA, HS245)
6-1. UsernamePasswordAuthenticationFilter는 login 요청시 동작확인

    - PrincipalDetails
    
    - PrincipalDetailsService
    
    - JwtAuthenticationFilter : filter 생성
      ([http://localhost:8080/login](http://localhost:8080/login) 요청시 extends UsernamePasswordAuthenticationFilter의 attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 메서드 호출)
    
    - SecurityConfig에 addFilter로 추가
---
### 회원가입 처리 빠진 듯 controller에 join 추가
---
6-2. attemptAuthentication() 메서드에서 로그인 처리
- 1. username과 password를 받는다.
    - ObjectMapper 이용하여 JSON을 객체로 매핑
- 2. 정상인지 로그인 시도 (AuthenticationManager로 로그인 시도하면 PrincipalDetailsService가 호출되고 loadUserByUsername()메서도 호출)

- 3. authentication를 세션에 담고(시큐리티에서 권한 관리를 위하여)
    - 권한 처리를 위하여 Authentication authentication = authenticationManager.authenticate(authenticationToken)을 리턴하여 세션에 넣는다
- 4. JWT 토큰을 만들어서 응답
    - attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication가 호출된다.
    - JWT토큰을 만들어서 request요청한 사용자에서 JWT토큰을 response 해준다.
    
## 7. JWT 토큰 만들고, JWT 보내면서 응답하기
        1. Authentication 객체를 가져와서 JWT 토큰을 만들 것이다.
        2. JWT Token 만들기 (JWT 라이브러리 이용 java-jwt) Builder 패턴
        3. response.addHeader에 Authorization:jwtToken 값 넣어서 확인

## 8. jwt토큰 서버 구축
    1. 로그인 후 클라이언트가 요청할 때, JWT 토큰이 유효한지 판단하는 필터
    → JwtAuthorizationFilter 커스텀화하기
    : 시큐리티가 filter를 가지고 있는데, 그 필터 중 BasicAuthenticationFilter
    권한이나 인증이 필요한 특정 주소를 요청했을 때, 위 필터를 무조건 타게 되어있음.
    만약 권한이 인증이 필요한 주소가 아니라면 이 필터를 안 거친다. 
    ( doFilterInternal() 메서드 동작한다 인증이나 권한이 필요한 주소로 요청시)
    2. doFilterInternal메서드에서 Header를 잘 받아오는지 확인
    3. Header에 jwtToken을 Authorization 키 값에 넣어서 클라이언트가 요청하면 서버에서 토큰 검증을 통해 확인
        (JWT 토큰 검증을 하여 정상적인 사용자인지 확인)
        제대로 된 헤더인지 확인->jwtToken 앞부분 Bearer 제거->secret 값 : commGom JWT 검증
        ->서명이 정상적으로 되었다면 -> JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 -> 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
## 9. JwtProperties 인터페이스에 상수로 값 지정해두기