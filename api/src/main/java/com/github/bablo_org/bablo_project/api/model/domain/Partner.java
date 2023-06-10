package com.github.bablo_org.bablo_project.api.model.domain;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partner {
    private String id;
    private Set<String> tags;
}
