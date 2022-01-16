package com.example.security02_jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security02_jwt.config.auth.PrincipalDetails;
import com.example.security02_jwt.model.User;
import com.example.security02_jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository){
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    //1.인증이나 권한이 필요한 주소 요청이 있을 때, 해당 필터를 타게됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨!");
//        super.doFilterInternal(request, response, chain); -> 지워야함
        //2.Header로 받아와서 확인할 것이다.
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader:" + jwtHeader);
//        3. JWT 토큰 검증을 하여 정상적인 사용자인지 확인
//        제대로 된 헤더인지 확인->jwtToken 앞부분 Bearer 제거->secret 값 : commGom JWT 검증
//        ->서명이 정상적으로 되었다면 -> JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 -> 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
        //제대로 된 헤더인지 확인
        if(jwtHeader==null||!jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);   //다시 필터를 타도록 넘긴다. 다른 과정은 안하도록
            return;
        }
        //jwtToken 앞부분 Bearer 제거
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        //secret 값 : commGom JWT 검증
        String username = JWT.require(Algorithm.HMAC512("commGom")).build().verify(jwtToken).getClaim("username").asString();

        //서명이 정상적으로 되었다면
        if (username!=null){
            User userEntity = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            //JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만든다
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            //강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request,response);
        }
    }
}
