package com.app.gbank.service;

import com.app.gbank.dto.AddFundsRequest;
import com.app.gbank.dto.CreateUserRequest;
import com.app.gbank.dto.DeductFundsRequest;
import com.app.gbank.dto.RegisterRequest;
import com.app.gbank.entity.User;
import com.app.gbank.enums.Role;
import com.app.gbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User register(RegisterRequest request) {
        User user = new User();
        user.setFullName(request.fullName());
        user.setMobileNumber(request.mobileNumber());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setBalance(BigDecimal.ZERO);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Transactional
    public User addUser(CreateUserRequest request) {
        User user = new User();
        user.setFullName(request.fullName());
        user.setMobileNumber(request.mobileNumber());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role() == null ? Role.ROLE_USER : request.role());
        user.setBalance(BigDecimal.ZERO);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Transactional
    public void toggleEnabled(Long userId) {
        User user = findById(userId);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @Transactional
    public void addFunds(Long userId, AddFundsRequest request) {
        User user = findById(userId);
        user.setBalance(user.getBalance().add(request.amount()));
        userRepository.save(user);
    }

    @Transactional
    public void deductFunds(Long userId, DeductFundsRequest request) {
        User user = findById(userId);
        BigDecimal current = user.getBalance();
        if (current.compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        user.setBalance(current.subtract(request.amount()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    
}