package com.github.bablo_org.bablo_project.api.client;

import com.github.bablo_org.bablo_project.api.model.currency_api.CurrencyInfo;

import java.util.List;

public interface CurrencyInfoClient {

    List<CurrencyInfo> getInfo();
}
