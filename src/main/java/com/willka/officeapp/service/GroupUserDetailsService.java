package com.willka.officeapp.service;

import com.willka.officeapp.model.User;
import com.willka.officeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class GroupUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<User> user= userRepository.findBYUserName(username);
        return user.map(GroupUserDetails::new).
                orElseThrow(()->new UsernameNotFoundException(username+"does not exist in system"));
    }
}
