package com.instagram.services;

import com.instagram.models.User;
import com.instagram.models.UserCredentials;
import com.instagram.models.UserProfile;
import com.instagram.repository.ProfileRepository;
import com.instagram.repository.UserCredentialsRepo;
import com.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    UserCredentialsRepo userCredentialsRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        UserProfile profile = profileRepository.findByUsername(username);

        if(profile == null){
            throw new UsernameNotFoundException("Could not find user");
        }

        UserCredentials user = profile.getUser();
        String roles[] = user.getRole().split(",");
        List<SimpleGrantedAuthority> rolesL = new ArrayList<>();
        for(String r:roles){
            rolesL.add(new SimpleGrantedAuthority(r));
        }


        return new org.springframework.security.core.userdetails.User(profile.getUsername(), user.getUserPassword(),user.isVerified(),true,true,true,rolesL);
    }

}