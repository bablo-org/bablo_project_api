package com.github.bablo_org.bablo_project.api.model.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Network {
    private Map<String, List<String>> partners = new HashMap<>();

    public static Network ofMap(Map<String, ?> map) {
        return map == null
               ? new Network()
               : new Network((Map<String, List<String>>) map);
    }

    public boolean isPartner(String partner) {
        return partners.containsKey(partner);
    }

    public void addPartner(String partner) {
        partners.put(partner, new ArrayList<>());
    }

    public void updateTags(String partner, List<String> tags) {
        partners.put(partner, tags);
    }
}
