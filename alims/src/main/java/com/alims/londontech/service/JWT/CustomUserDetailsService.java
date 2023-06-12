package com.alims.londontech.service.JWT;

import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByName(username);
        if (userAccount == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(userAccount.getUsername(), userAccount.getPassword(), new ArrayList<>());}
}
