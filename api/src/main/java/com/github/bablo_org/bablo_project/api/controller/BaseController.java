package com.github.bablo_org.bablo_project.api.controller;

import com.github.bablo_org.bablo_project.api.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public abstract class BaseController {

    @ExceptionHandler(Exception.class)
    ResponseEntity<Error> onException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity
                .internalServerError()
                .body(new Error(ex.getMessage()));
    }
}
