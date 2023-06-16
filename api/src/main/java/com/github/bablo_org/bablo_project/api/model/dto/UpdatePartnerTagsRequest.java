package com.github.bablo_org.bablo_project.api.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePartnerTagsRequest {
    private String partnerId;
    private List<String> tags;
}
