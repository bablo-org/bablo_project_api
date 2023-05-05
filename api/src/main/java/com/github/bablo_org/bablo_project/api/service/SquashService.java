package com.github.bablo_org.bablo_project.api.service;

import com.github.bablo_org.bablo_project.api.model.SquashData;

import java.util.List;

public interface SquashService {

    public List<SquashData> generate(String user, String currency);
}
