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

확인 인증이 필요없는 url의 경우 session이 없기 때문에 바로 실행된다.

권한이 필요한 것은 아직 실행되지않음.(403 error)