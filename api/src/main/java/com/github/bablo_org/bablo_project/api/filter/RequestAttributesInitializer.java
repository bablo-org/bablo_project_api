package com.github.bablo_org.bablo_project.api.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bablo_org.bablo_project.api.Constants;
import com.github.bablo_org.bablo_project.api.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestAttributesInitializer implements Filter {

    private final FirebaseAuth firebaseAuth;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            if (!req.getMethod().equals("OPTIONS")) {
                String authorization = req.getHeader("Authorization");

                if (StringUtils.isBlank(authorization)) {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    res.getWriter().write("unauthorized");
                    return;
                }

                String token = authorization.contains(" ")
                        ? authorization.split(" ")[1]
                        : authorization;
                FirebaseToken userToken = firebaseAuth.verifyIdToken(token);
                request.setAttribute(Constants.USER_TOKEN, userToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(e.getMessage());
        }
    }
}