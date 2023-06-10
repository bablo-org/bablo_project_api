package com.github.bablo_org.bablo_project.api.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.Constants;
import com.github.bablo_org.bablo_project.api.model.dto.Error;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final FirebaseAuth firebaseAuth;

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            if (isJob(req)) {
                checkJobAuth(req);
            } else if (isProtectedUserRequest(req)) {
                checkUserAuth(req);
            }
        } catch (Exception e) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            Error error = new Error(e.getMessage());
            res.getWriter().write(mapper.writeValueAsString(error));
            return;
        }

        filterChain.doFilter(req, res);
    }

    private boolean isJob(HttpServletRequest request) {
        return request.getRequestURI().contains("/jobs/");
    }

    private boolean isProtectedUserRequest(HttpServletRequest request) {
        return !request.getMethod().equals(HttpMethod.OPTIONS.name()) // preflight requests
                && !request.getRequestURI().contains("/swagger")      // documentation
                && !request.getRequestURI().contains("/api-docs");
    }

    @SneakyThrows
    private void checkJobAuth(HttpServletRequest request) {
        String token = getRequiredToken(request);
        GoogleIdToken idToken = googleIdTokenVerifier.verify(token);
        log.info("Job launched with user: " + idToken.getPayload().getEmail());
    }

    @SneakyThrows
    private void checkUserAuth(HttpServletRequest request) {
        String token = getRequiredToken(request);
        FirebaseToken userToken = firebaseAuth.verifyIdToken(token);

        request.setAttribute(Constants.USER_TOKEN, userToken);
    }

    private String getRequiredToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (StringUtils.isBlank(authorization)) {
            throw new RuntimeException(
                    "authorization token must be provided for this request (via 'Authorization' header)");
        }

        return authorization.contains(" ")
               ? authorization.split(" ")[1]
               : authorization;
    }
}