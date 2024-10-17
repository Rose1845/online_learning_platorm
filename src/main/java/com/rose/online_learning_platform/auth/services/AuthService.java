package com.rose.online_learning_platform.auth.services;

import com.github.f4b6a3.ulid.UlidCreator;
import com.rose.online_learning_platform.auth.dto.*;
import com.rose.online_learning_platform.auth.entities.AuthToken;
import com.rose.online_learning_platform.auth.entities.Role;
import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.auth.repository.AuthTokenRepository;
import com.rose.online_learning_platform.auth.repository.RoleRepository;
import com.rose.online_learning_platform.auth.repository.UserRepository;
import com.rose.online_learning_platform.commons.enums.ResponseStatusEnum;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.responses.GenericResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthService jwtAuthService;
    private final AuthTokenRepository authTokenRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public void saveUserToken(User user, JwtTokenOutput token) {
        try {
            AuthToken authToken = new AuthToken();
            authToken.setUserId(user);
            authToken.setToken(token.getRefreshToken());
            authToken.setExpiryDate(token.getExpiresIn());
            authToken.setIssuedAt(token.getIssuedAt());
            authToken.setIsExpired(false);
            authToken.setIsRevoked(false);
            authTokenRepository.save(authToken);
        } catch (RuntimeException e) {
            log.error("Error saving user token", e);
            throw new RuntimeException("Error logging in user");
        }

    }

    public GenericResponse<UserDTO> register(RegistrationDTO request) throws EMException {

        if(userRepository.existsUserByEmail(request.getEmail())){
            throw  EMException.builder().message("Email already exists").metadata("email/phone number exists").build();
        }


        String userId = UlidCreator.getUlid().toLowerCase();

        User user = new User();

        Optional<Role> optionalRole = roleRepository.findByName(RolesEnum.USER);

        if (optionalRole.isEmpty()) {
            throw new IllegalArgumentException("Role not found");
        }

        user.setId(userId)
                .setFullName(request.getFullName())
                .setEmail(request.getEmail())
                .setRoles(Set.of(optionalRole.get()))
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setAcceptTerms(true);
        log.info("user",user);
        User savedUser = userRepository.save(user);
        UserDTO userDTO =  new UserDTO()
                .setId(savedUser.getId())
                .setFullName(request.getFullName())
                .setEmail(request.getEmail())
                .setAcceptTerms(request.getAcceptTerms())
                .setCreatedAt(savedUser.getCreatedAt())
                .setUpdatedAt(savedUser.getUpdatedAt());
        return GenericResponse.<UserDTO>builder()
                .message("user registered successfully")
                .status(ResponseStatusEnum.SUCCESS)
                .debugMessage("user registration")
                .data(userDTO)
                .build();
    }

    public GenericResponse<LoginResponseDTO> authenticate(String cred, String password, HttpServletResponse response) throws EMException {
        try {
            User user = userRepository.findByEmail(cred).orElseThrow(() -> EMException.builder()
                    .message("User not found!!")
                    .build());

            if (!user.isAccountNonLocked() || !user.isAccountNonExpired() || !user.isCredentialsNonExpired() || !user.isActive()){
                throw  EMException.builder().message("Account is locked").build();
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw  EMException.builder().message("Invalid Credentials!!").build();
            }

            Authentication authentication = null;

            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getEmail(), password)
                );
            } catch (Exception e) {
                throw  EMException.builder().message(e.getMessage()).build();
            }

            if (authentication == null) {
                throw  EMException.builder().message("User Not found").statusCode(404).build();
            }

            User principal = (User) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtTokenOutput jwtOutput = jwtAuthService.generateJwtResponse(principal);

            saveUserToken(user, jwtOutput);

            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setAccessToken(jwtOutput.getAccessToken());
            authResponseDTO.setRefreshToken(jwtOutput.getRefreshToken());

            UserDTO userDTO = userService.convertToDto(principal);
            Cookie cookie = new Cookie("refreshToken", jwtOutput.getRefreshToken());
            cookie.setMaxAge(60 *60 * 24);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setSecure(true);
            response.addCookie(cookie);

            var loginres =new  LoginResponseDTO(userDTO, authResponseDTO);

            return GenericResponse.<LoginResponseDTO>builder()
                    .data(loginres)
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("login successfully")
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid credentials--->", e);
            throw  EMException.builder().message(e.getMessage()).build();
        }

    }

    public boolean validateToken(AuthTokenDto data) {
        return jwtAuthService.isTokenValid(data.getAccessToken());
    }

    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        // verify the refresh token
        // get the user details from the refresh token
        // generate a new access token
        jwtAuthService.isTokenValid(refreshToken, null);

        return new AuthResponseDTO();
    }
}
