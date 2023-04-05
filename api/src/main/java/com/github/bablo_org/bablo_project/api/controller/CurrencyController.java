package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController extends BaseController {

    private final CurrencyService service;

    @GetMapping
    @ResponseBody
    List<Currency> getAll(@RequestParam(value = "isActive", required = false) Boolean isActive) {
        return isActive == null
               ? service.getAll()
               : service.getByStatus(isActive);
    }

    @PutMapping("/activate")
    @ResponseBody
    ResponseEntity<String> activate(@RequestBody List<String> ids, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.changeStatus(ids, true, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/deactivate")
    @ResponseBody
    ResponseEntity<String> deactivate(@RequestBody List<String> ids, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.changeStatus(ids, false, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }
}
