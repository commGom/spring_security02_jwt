package com.example.security02_jwt.config.jwt;

public interface JwtProperties {
    // 토큰 만들 때 .sign(Algorithm.HMAC512("commGom"));   //내 서버만 아는 고유한 시크릿 키 값(sign하기 위해서)
    // 토큰 점검 할 때
    // //jwtToken 앞부분 Bearer 제거
    //        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
    //        //secret 값 : commGom JWT 검증
    //        String username = JWT.require(Algorithm.HMAC512("commGom")).build().verify(jwtToken).getClaim("username").asString();

    String SECRET = "commGom";      //우리 서버만 알고 있는 비밀 값
    int EXPIRATION_TIME=60000*10;   //10분
    String TOKEN_PREFIX="Bearer ";
    String HEADER_STRING = "Authorization";

    //정의 한 후 JwtProperties.SECRET 과 형식으로 값을 가져와서 사용하기
}
