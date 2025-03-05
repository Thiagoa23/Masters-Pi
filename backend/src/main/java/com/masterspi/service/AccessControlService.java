package com.masterspi.service;

import com.masterspi.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class AccessControlService {

    public boolean temPermissao(User user, String role) {
        return getAuthorities(user).contains(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

}
