package com.github.bablo_org.bablo_project.api.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @JsonProperty("id")
    @SerializedName("id")
    public long id;

    @JsonProperty("first_name")
    @SerializedName("first_name")
    public String firstName;

    @JsonProperty("last_name")
    @SerializedName("last_name")
    public String lastName;

    @JsonProperty("username")
    @SerializedName("username")
    public String userName;

    @JsonProperty("type")
    @SerializedName("type")
    public String type;
}
