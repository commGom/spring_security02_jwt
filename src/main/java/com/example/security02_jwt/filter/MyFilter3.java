package com.example.security02_jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 3");
//        1. downcasting해서 하기
//        2. 요청의 헤더에서 Authorization 값 받아오기
//        3. 요청 방식이 post일 때만 동작 시키기위한 조건문 작성
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        if (req.getMethod().equals("POST")){
            filterChain.doFilter(req,res);   //계속 이어서 하도록 하기 위해서 chain 이용
//            String headerAuth = req.getHeader("Authorization");
//            System.out.println(headerAuth);
//
//            if (headerAuth.equals("course")){
//                filterChain.doFilter(req,res);   //계속 이어서 하도록 하기 위해서 chain 이용
//            }else{
//                PrintWriter printWriter=res.getWriter();
//                printWriter.println("인증 안 됨");
//            }

        }

    }
}
