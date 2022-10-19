package com.divizia.dbconstructor.model.service.impl;

import com.divizia.dbconstructor.model.entity.User;
import com.divizia.dbconstructor.model.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return new org.springframework.security.core.userdetails.User(
                user.getId(), user.getPassword(), Set.of(user.getRole())
        );
    }
}
