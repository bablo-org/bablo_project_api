package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;

import lombok.Data;

@Data
public class Squash {
    private String id;
    private SquashStatus status;
    private Date created;
    private Date updated;

    private SquashData data;
}
