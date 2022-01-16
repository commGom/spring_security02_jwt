package com.example.security02_jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security02_jwt.config.auth.PrincipalDetails;
import com.example.security02_jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// Spring security에 UsernamePasswordAuthenticationFilter가 있어서
// login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작을 한다
// addFilter로 해당 필터를 등록해준다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도 중");

        //        1.username과 password를 받는다.
        try {
//            BufferedReader br = request.getReader();
//
//            String input=null;
//            while ((input=br.readLine())!=null){
//                System.out.println(input);
//        }
            ObjectMapper om=new ObjectMapper(); //JSON 데이터를 JAVA 객체로 Parsing을 해준다(Message converter)
            User userEntity = om.readValue(request.getInputStream(), User.class);
            System.out.println(userEntity);
            //        2.정상인지 로그인 시도 (AuthenticationManager로 로그인 시도하면 PrincipalDetailsService가 호출되고 loadUserByUsername()메서도 호출)

            //토큰 만들기
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userEntity.getUsername(), userEntity.getPassword());
            //PrincipalDetailsService의 loadUserByUsername() 함수가 호출된다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //        3.PrincipalDetails를 세션에 담고(시큐리티에서 권한 관리를 위하여)
            PrincipalDetails principalDetails= (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());
            //토큰이용 후 로그인 시도 하고 난 후에 Authentication객체가 session 영역에 저장이 된다. => 로그인이 되었다.
            // Session을 만드는 이유 (권한 처리를 위하여 JWT는 Session 만들이유 없다. stateless)

            //        4.JWT 토큰을 만들어서 응답(uccessfulAuthentication)
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=======================");
        return null;
    }

//    attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication가 호출된다.
//    JWT토큰을 만들어서 request요청한 사용자에서 JWT토큰을 response 해준다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증이 완료 되었음 JWT TOKEN response 해주면된다.");

//        JWT Token 만들고 response 하기
//        1. Authentication 객체를 가져와서 JWT 토큰을 만들 것이다.
//        2. JWT Token 만들기 (JWT 라이브러리 이용 java-jwt) Builder 패턴
//        3. response.addHeader에 Authorization:jwtToken 값 넣어서 확인
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken= JWT.create()
                .withSubject("토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000)*10)) //토큰 만료시간 10분
                .withClaim("id",principalDetails.getUser().getId())
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("commGom"));   //내 서버만 아는 고유한 시크릿 키 값(sign하기 위해서)

        response.addHeader("Authorization","Bearer "+jwtToken);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
