package com.kroger.csp.ui.config;

import brave.Tracer;
import com.kroger.commons.error.AbstractGlobalExceptionHandler;
import com.kroger.commons.error.ApiError;
import com.kroger.commons.error.EnableMissingWebRouteSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@EnableMissingWebRouteSupport
public class GlobalExceptionHandler extends AbstractGlobalExceptionHandler
{
    @Autowired
    public GlobalExceptionHandler(@Autowired(required = false)Tracer tracer)
    {
        super(tracer);
    }

    @ExceptionHandler(org.springframework.data.mapping.PropertyReferenceException.class)
    public ResponseEntity<ApiError> handleHttpExceptions(org.springframework.data.mapping.PropertyReferenceException ex,
                                                         WebRequest webRequest)
    {
        ApiError apiError = ApiError.of(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage());
        addTraceContext(apiError);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.oauth2.common.exceptions.OAuth2Exception.class)
    public ResponseEntity<ApiError> handleHttpExceptions(org.springframework.security.oauth2.common.exceptions.OAuth2Exception ex,
                                                         WebRequest webRequest)
    {
        ApiError apiError = ApiError.of(ex.getOAuth2ErrorCode(),
                ex.getLocalizedMessage());
        addTraceContext(apiError);
        return new ResponseEntity<>(apiError, HttpStatus.resolve(ex.getHttpErrorCode()));
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationExceptions(
            org.springframework.security.core.AuthenticationException ex,
            WebRequest webRequest)
    {
        ApiError apiError = ApiError.of(HttpStatus.UNAUTHORIZED,
                "User name or password is incorrect.");
        addTraceContext(apiError);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAuthenticationExceptions(
            org.springframework.security.access.AccessDeniedException ex,
            WebRequest webRequest)
    {
        ApiError apiError = ApiError.of(HttpStatus.FORBIDDEN,
                "Sorry. You're not authorized to enter the area you tried to reach.");
        addTraceContext(apiError);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}