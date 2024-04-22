package com.willka.officeapp.controller;

import com.willka.officeapp.model.User;
import com.willka.officeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final String DEFAULT_ROLE = "ROLES_USER";
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostMapping("/join")
    public String joinGroup(@RequestBody User user){
        user.setRoles(DEFAULT_ROLE);
        String encryptedPwd=bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        userRepository.save(user);
        return "Hi "+user.getUserName()+"welcome to group !";
    }
}
