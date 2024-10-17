package com.rose.online_learning_platform.auth.controllers;

import com.rose.online_learning_platform.auth.dto.*;
import com.rose.online_learning_platform.auth.services.AuthService;
import com.rose.online_learning_platform.commons.enums.ResponseStatusEnum;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.responses.GenericResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<UserDTO>> registerUser (@Valid @RequestBody RegistrationDTO req) throws EMException {

        try {
            return ResponseEntity.ok(authService.register(req));

        } catch (EMException e){
            GenericResponse<UserDTO> errorResponse = GenericResponse.<UserDTO>builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }
        catch (Exception e) {
            GenericResponse<UserDTO> errorResponse = GenericResponse.<UserDTO>builder()
                    .data(null)
                    .message("An unexpected error occurred")
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login A user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class)) }),
    })
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginResponseDTO>> loginUser(@RequestBody LoginDetailsDTO loginDetailsDTO, HttpServletResponse response) throws EMException {
        log.debug("Login request received for email: {}", loginDetailsDTO.getEmail());
        try {
            log.debug("Login successful for email: {}", loginDetailsDTO.getEmail());
            return  ResponseEntity.ok(authService.authenticate(loginDetailsDTO.getEmail(), loginDetailsDTO.getPassword(), response));
        } catch (EMException e) {
            log.error("Login failed for email: {}", loginDetailsDTO.getEmail(), e);
            GenericResponse<LoginResponseDTO> errorResponse   =   GenericResponse.<LoginResponseDTO>builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage("failed login")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (Exception e) {
                    GenericResponse<LoginResponseDTO> errorResponse  = GenericResponse.<LoginResponseDTO>builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage("Unexpected error during login")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping(value="/validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken(@Valid @RequestBody AuthTokenDto token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }
}
