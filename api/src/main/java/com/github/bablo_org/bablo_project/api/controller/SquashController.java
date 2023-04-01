package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.SquashData;
import com.github.bablo_org.bablo_project.api.service.SquashService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/squashes", produces = MediaType.APPLICATION_JSON_VALUE)
public class SquashController extends BaseController {

    private final SquashService service;

    @GetMapping("/generate")
    List<SquashData> generate(@RequestParam("currency") String currency,
                              @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.generate(userToken.getUid(), currency);
    }
}
