package com.lab.blps.xml;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class XmlUserDetailsService implements UserDetailsService {

    private final XmlUserService xmlUserService;

    public XmlUserDetailsService(XmlUserService xmlUserService) {
        this.xmlUserService = xmlUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return xmlUserService.findByUsername(username)
                .map(user -> {
                    System.out.println("Loaded user: " + user.getUsername());
                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
                    return new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            Collections.singletonList(authority)
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

}
