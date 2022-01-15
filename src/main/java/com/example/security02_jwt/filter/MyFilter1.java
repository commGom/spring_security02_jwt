package com.example.security02_jwt.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 1");
        filterChain.doFilter(servletRequest,servletResponse);   //계속 이어서 하도록 하기 위해서 chain 이용

    }
}
