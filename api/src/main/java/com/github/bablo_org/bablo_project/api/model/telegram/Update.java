package com.github.bablo_org.bablo_project.api.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Update {

    @JsonProperty("update_id")
    @SerializedName("update_id")
    public long updateId;

    @JsonProperty("message")
    @SerializedName("message")
    public Message message;
}
