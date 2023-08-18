package com.example.demo.common;

import com.example.demo.user.ApplicationUser;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class AuditorAwareImpl implements AuditorAware<ApplicationUser> {

    @Autowired
    private UserRepository userRepository;
    @Override
    public Optional<ApplicationUser> getCurrentAuditor() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(Long.parseLong(id));
    }
}
