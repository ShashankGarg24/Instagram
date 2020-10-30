package com.instagram.controllers;

import com.instagram.Configuration.JwtUtil;
import com.instagram.models.LoginRequest;
import com.instagram.models.LoginResponse;
import com.instagram.models.User;
import com.instagram.serviceImpl.UserServiceImpl;
import com.instagram.services.UserService;
import com.instagram.services.VerificationMail;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Login {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private VerificationMail mailService;

    @PostMapping("/login")
    public ResponseEntity<?> createLoginToken(@RequestBody LoginRequest request) throws Exception {
        System.out.println(request);
        try {

            User user = userService.findUserByUsername(request.getUsername());
            if(user == null){
                return new ResponseEntity<>("Username doesn't exist!", HttpStatus.NOT_FOUND);
            }
            System.out.println(user.isVerified());
            if (!user.isVerified()) {
                mailService.sendVerificationEmail(user);
                userService.updateUser(user);
                return new ResponseEntity<String>("User not verified. Please check EMAIL to verify.",
                        HttpStatus.NOT_ACCEPTABLE);
            }

        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<String>("Username or password is wrong...", HttpStatus.NOT_FOUND);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.getUsername());

        if (userDetails.isEnabled()) {
            User user = userService.findUserByUsername(request.getUsername());
            final String jwt = jwtTokenUtil.generateToken(userDetails);
            return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User is disabled by ADMIN.", HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {

        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        final String jwt = jwtTokenUtil.generateRefreshToken(claims);
        User user = userService.findUserByUsername(jwtTokenUtil.getUsernameFromToken(jwt));
        System.out.println("Token refreshed");
        return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user), HttpStatus.OK);

    }


    @GetMapping("/getUser/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username){
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }
}
