package com.app.gbank.security;

import com.app.gbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        return userRepository.findByMobileNumber(mobileNumber)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with mobile number: " + mobileNumber));
    }
}