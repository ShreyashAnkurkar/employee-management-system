package com.nit.service.impl;

import com.nit.dto.request.LoginRequest;
import com.nit.dto.request.RegisterRequest;
import com.nit.dto.response.AuthResponse;
import com.nit.entity.Employee;
import com.nit.entity.Role;
import com.nit.entity.User;
import com.nit.exception.ResourceNotFoundException;
import com.nit.repository.EmployeeRepository;
import com.nit.repository.RoleRepository;
import com.nit.repository.UserRepository;
import com.nit.security.JwtService;
import com.nit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

        User.UserBuilder userBuilder = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role);

        // Link to employee if provided
        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));
            userBuilder.employee(employee);
        }

        User saved = userRepository.save(userBuilder.build());
        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getUsername());
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .username(saved.getUsername())
                .role(saved.getRole().getName().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().getName().name())
                .build();
    }
}