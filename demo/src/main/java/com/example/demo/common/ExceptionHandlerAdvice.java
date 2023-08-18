package com.example.demo.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.transform.Result;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<?> handleAuthenticationException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail", "User name or password is incorrect", e.getMessage()));
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<?> handleAccountStatusException(AccountStatusException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail", "User account is abnormal", e.getMessage()));
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, PreAuthenticatedCredentialsNotFoundException.class, AuthenticationCredentialsNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<?> handleAccountStatusException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail", "User is not authorized", e.getMessage()));
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<?> handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail", "The access token provided is expired, revoked, malforemd or invalid for other reason", e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject<>("fail", "No permission", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<?> handleOtherException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail", "A server internal error occurs", e.getMessage()));
    }
}
