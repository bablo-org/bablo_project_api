package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.ORIGIN_URL;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = ORIGIN_URL)
@RequestMapping(path = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController extends BaseController {

    private final CurrencyService service;

    @GetMapping
    @ResponseBody
    List<Currency> getAll() {
        return service.getAll();
    }
}
