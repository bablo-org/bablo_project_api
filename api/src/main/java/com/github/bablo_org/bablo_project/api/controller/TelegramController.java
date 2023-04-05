package com.github.bablo_org.bablo_project.api.controller;

import com.github.bablo_org.bablo_project.api.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/telegram", produces = MediaType.APPLICATION_JSON_VALUE)
public class TelegramController extends BaseController {

    private final TelegramService service;

    @GetMapping("/resolve-id/{username}")
    @ResponseBody
    String resolveId(@PathVariable("username") String username) {
        return service.resolveId(username);
    }
}
