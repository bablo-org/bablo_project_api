package com.github.bablo_org.bablo_project.api.controller;

import java.util.List;
import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController extends BaseController {

    private final CurrencyService service;


    @GetMapping
    @ResponseBody
    List<Currency> getAll() {
        return service.getAll();
    }


    @PutMapping("/updateRates")
    void updateRates() {
        service.updateRates();
    }

    @PutMapping("/updateInfo")
    void updateInfo() {
        service.updateInfo();
    }
}
