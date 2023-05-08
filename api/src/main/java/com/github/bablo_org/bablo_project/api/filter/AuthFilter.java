package com.github.bablo_org.bablo_project.api.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bablo_org.bablo_project.api.Constants;
import com.github.bablo_org.bablo_project.api.utils.StringUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final FirebaseAuth firebaseAuth;


    private final GoogleIdTokenVerifier googleIdTokenVerifier;


    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            if (isProtected(req)) {
                String authorization = req.getHeader("Authorization");

                if (StringUtils.isBlank(authorization)) {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    res.getWriter().write("unauthorized");
                    return;
                }

                String token = authorization.contains(" ")
                        ? authorization.split(" ")[1]
                        : authorization;

                if (isJob(req)) {
                    GoogleIdToken idToken = googleIdTokenVerifier.verify(token);
                    log.info("Job launched with user: "+idToken.getPayload().getEmail());
                } else {
                    FirebaseToken userToken = firebaseAuth.verifyIdToken(token);
                    request.setAttribute(Constants.USER_TOKEN, userToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    private boolean isProtected(HttpServletRequest request) {
        return !request.getMethod().equals("OPTIONS") // exclude preflight requests
                && !request.getRequestURI().contains("/swagger")
                && !request.getRequestURI().contains("/api-docs"); // exclude docs endpoint
    }

    private boolean isJob(HttpServletRequest request) {
        return request.getRequestURI().contains("/job/");
    }


}