package com.der.logintemplate.service;

import com.der.logintemplate.model.Client;
import com.der.logintemplate.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService implements UserDetailsService {
    @Autowired
    private ClientRepository clientRepository;

    private Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username);

        if (client == null){
            logger.error("Login error: The client " + username + " doesn't exist");
            throw new UsernameNotFoundException("Login error: The client " + username + " doesn't exist");
        }

        List<GrantedAuthority> authorities = client.getRoles()
                .stream()
                .map(role -> {
                    return new SimpleGrantedAuthority(role.getName());
                })
                .peek(authority -> logger.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());

        return new User(client.getUsername(), client.getPassword(), client.getEnabled(), true, true, true, authorities);
    }
}
