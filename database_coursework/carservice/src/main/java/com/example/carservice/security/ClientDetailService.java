package com.example.carservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientDetailService implements UserDetailsService {
    private final ClientRepository clientRepository;
    @Autowired
    public ClientDetailService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = clientRepository.findByUsername(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new ClientDetails(user.get());
    }

    public boolean check(String s) {
        Optional<User> person = clientRepository.findByUsername(s);
        return person.isEmpty();
    }
}
