package com.example.security02_jwt.config.auth;

import com.example.security02_jwt.model.User;
import com.example.security02_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//로그인 요청이 올 때 동작을 한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService 동작, username 값:"+username);
        User userEntity = userRepository.findByUsername(username);
        System.out.println(userEntity);
        return new PrincipalDetails(userEntity);
    }
}
