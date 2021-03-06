package com.example.security02_jwt.controller;

import com.example.security02_jwt.model.User;
import com.example.security02_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("/token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입 완료";
    }

    //user, manager, admin 권한 접근 가능
    @PostMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    //mangaer, admin 접근 가능
    @PostMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    //admin 만 접근 가능
    @PostMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
