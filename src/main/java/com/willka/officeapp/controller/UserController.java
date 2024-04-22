package com.willka.officeapp.controller;

import com.willka.officeapp.common.UserConstant;
import com.willka.officeapp.model.User;
import com.willka.officeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostMapping("/join")
    public String joinGroup(@RequestBody User user){
        user.setRoles(UserConstant.DEFAULT_ROLE);
        String encryptedPwd=bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        userRepository.save(user);
        return "Hi "+user.getUserName()+"welcome to group !";
    }
        //if loggedin user is ADMIN ->ADMIN OR MODERATOR
        //if loggedin user is MODERATOR -> MODERATOR

    @GetMapping("/access/{userId}/{userRole}")
    //@Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String giveAccessToUser(@PathVariable int userId, @PathVariable String userRole, Principal principal){
        User user=userRepository.findById(userId).get();
        List<String> activeRoles=getRoleByLoggedInUser(principal);
        String newRole="";
        if(activeRoles.contains(userRole)){
            newRole=user.getRoles()+","+userRole;
            user.setRoles(newRole);
        }
        return  null;
    }

    private List<String> getRoleByLoggedInUser(Principal principal){
        String roles=getLoggedInUser(principal).getRoles();
        List<String> assignRoles= Arrays.stream(roles.split(",")).toList();
        if(assignRoles.contains("ROLE_ADMIN")){
            return  Arrays.stream(UserConstant.ADMIN_ACCESS).toList();
        }
        if(assignRoles.contains("ROLE_MODERATOR")){
            return  Arrays.stream(UserConstant.MODERATOR_ACCESS).toList();
        }
        return Collections.emptyList();
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> loadUsers(){
        return  userRepository.findAll();
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String testUserAccess(){
        return  "user can only access this !";
    }

    private User getLoggedInUser(Principal principal){
        return userRepository.findBYUserName(principal.getName()).get();
    }

}
