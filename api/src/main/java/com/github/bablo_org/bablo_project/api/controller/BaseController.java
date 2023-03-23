package com.github.bablo_org.bablo_project.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseController {

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> onException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(ex.getMessage());
    }
}
