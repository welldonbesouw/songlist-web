package com.unseensonglist.songlist.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.unseensonglist.songlist.model.ERole;
import com.unseensonglist.songlist.model.Role;
import com.unseensonglist.songlist.model.User;
import com.unseensonglist.songlist.payload.request.LoginRequest;
import com.unseensonglist.songlist.payload.request.RegisterRequest;
import com.unseensonglist.songlist.payload.response.JwtResponse;
import com.unseensonglist.songlist.repository.RoleRepository;
import com.unseensonglist.songlist.repository.UserRepository;
import com.unseensonglist.songlist.security.jwt.JwtUtils;
import com.unseensonglist.songlist.security.service.UserDetailsImpl;

@Service
public class UserService {
	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepo;

	public JwtResponse authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);

		return jwtResponse;
	}

	public User registerUser(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		if (registerRequest.getPassword() == registerRequest.getConfirmPassword()) {
			user.setPassword(encoder.encode(registerRequest.getPassword()));
		}
		Set<String> strRoles = registerRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepo.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "Admin":
					Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;

				default:
					Role userRole = roleRepo.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		return user;
	}

	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	public void saveUser(User user) {
		userRepo.save(user);

	}
}
