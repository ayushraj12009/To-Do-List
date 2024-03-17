package com.apiwiz.Controller;

import com.apiwiz.Config.JwtProvider;
import com.apiwiz.Model.User;
import com.apiwiz.Repository.UserRepository;
import com.apiwiz.Service.CustomerUserDetailsService;
import com.apiwiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody User user) {
        try {
            // Check user with the same email already exists or not in DB
            User isExist = userRepository.findByEmail(user.getEmail());
            if (isExist != null) {
                throw new Exception("Email already used with another account");
            }

            // if not then Create a new user
            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setFristName(user.getFristName());
            newUser.setLastName(user.getLastName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save the new user to the database
            User savedUser = userRepository.save(newUser);

            // Generate JWT token for the newly registered user
            Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
            String token = JwtProvider.generateToken(authentication);

            // Create and return response with the generated token and success message
            AuthResponse response = new AuthResponse(token, "Signup Success");
            return response;
        } catch (Exception e) {
            // If any exception occurs during signup, return response with error message
            return new AuthResponse(null, e.getMessage());
        }
    }


    @PostMapping("/signin")
    public AuthResponse signin(@RequestBody LogginRequest logginRequest){
        try {
            // Authenticate user with provided email and password
            Authentication authentication = authenticate(logginRequest.getEmail(), logginRequest.getPassword());

            // Generate JWT token for the authenticated user
            String token = JwtProvider.generateToken(authentication);

            // Create and return response with the generated token and success message
            AuthResponse response = new AuthResponse(token, "Login Success");
            return response;
        }
        catch (Exception e){
            // If any exception occurs during signin, return response with error message
            return new AuthResponse(null, e.getMessage());
        }
    }



    private Authentication authenticate(String email, String password) {

        // this if statement is only for junit test case
//        if(email == "ayushraj12009@gmail.com"){
//            User user = new User("ayushraj12009@gmail.com", "AyushRaj@#12009");
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            UserDetails userDetails =  new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),authorities);
//            return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
//        }


        // Load UserDetails from database using CustomerUserDetailsService
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid UserName");
        }
        // Check if provided password matches the stored password after encoding
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }

        // Return UsernamePasswordAuthenticationToken with UserDetails and authorities
       // List<GrantedAuthority> authorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    }





}



