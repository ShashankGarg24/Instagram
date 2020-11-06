package com.instagram.controllers;

import com.instagram.Configuration.JwtUtil;
import com.instagram.models.*;
import com.instagram.repository.ProfileRepository;
import com.instagram.repository.UserCredentialsRepo;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class Login {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserCredentialsRepo userCredentialsRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private VerificationMail mailService;

    private HashMap<String, String> map = new HashMap<>();


    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        System.out.println(request);
        try {
            UserCredentials user;
            if (request.getUsername().contains("@")) {
                user = userService.findUserByEmail(request.getUsername());//username = username or email
                checkAccountAndPassword(user, request);

                System.out.println(user.isVerified());

                if (!user.isVerified()) {
                    mailService.sendVerificationEmail(user);
                    return new ResponseEntity<String>("User not verified. Please check EMAIL to verify.",
                            HttpStatus.NOT_ACCEPTABLE);
                }

                map.put(request.getUsername(), request.getPassword());
                return new ResponseEntity<>(user.getProfiles(), HttpStatus.OK);

            } else {
                System.out.println(1);
                UserProfile profile = profileRepository.findByUsername(request.getUsername());
                System.out.println(2);
                user = userCredentialsRepo.findByProfilesProfileId(profile.getProfileId());
                System.out.println(3);
                checkAccountAndPassword(user, request);
                map.put(user.getUserEmail(), request.getPassword());
                return createLoginToken(profileRepository.findByUsername(request.getUsername()),request.getPassword());
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/chooseProfile")
    public ResponseEntity<?> chooseProfile(@RequestBody Map<String, String> request){

        UUID profileId = UUID.fromString(request.get("id"));
        UserProfile profile = profileRepository.findByProfileId(profileId);
        UserCredentials user = userCredentialsRepo.findByProfilesProfileId(profileId);
        System.out.println(map.get(user.getUserEmail()));
        return createLoginToken(profile, map.get(user.getUserEmail()));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/allProfiles")
    public ResponseEntity<?> getAllProfiles(@RequestBody Map<String, String> request){
        System.out.println(map);
        UUID profileId = UUID.fromString(request.get("id"));
        UserCredentials user = userCredentialsRepo.findByProfilesProfileId(profileId);
        return new ResponseEntity<>(user.getProfiles(), HttpStatus.OK);
    }
    private void checkAccountAndPassword(UserCredentials user, LoginRequest request) throws Exception {

        if (user == null) {

            ResponseEntity.status(404);
            throw new Exception("Username/Email doesn't exist!");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean check = encoder.matches(request.getPassword(), user.getUserPassword());

        if (!check) {
            ResponseEntity.status(400);
            throw new Exception("Password is incorrect");        }

        return;
    }


    private ResponseEntity<?> createLoginToken(UserProfile profile, String password) {
        try {

            System.out.println(password);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(profile.getUsername(), password)
            );

            //map.remove(userCredentialsRepo.findByProfilesProfileId(profile.getProfileId()).getUserEmail());
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(profile.getUsername());

            if (userDetails.isEnabled()) {
                final String accessToken = jwtTokenUtil.generateToken(userDetails.getUsername());
                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());
                return new ResponseEntity<LoginResponse>(new LoginResponse(accessToken, refreshToken, profile), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("User is disabled by ADMIN.", HttpStatus.BAD_REQUEST);
            }
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(400));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<String>("bad credential", HttpStatus.valueOf(410));//improvisation required
        }
    }

    public void newPassword(String email, String password){
        System.out.println(map.get(email));
        map.put(email, password);
        System.out.println(map.get(email));
    }
 /*   @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {

        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        final String jwt = jwtTokenUtil.generateRefreshToken(claims);
        User user = userService.findUserByUsername(jwtTokenUtil.getUsernameFromToken(jwt));
        System.out.println("Token refreshed");
        return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user), HttpStatus.OK);

    }
*/


}
