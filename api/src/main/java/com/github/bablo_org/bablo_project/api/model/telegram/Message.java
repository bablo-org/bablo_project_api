package com.github.bablo_org.bablo_project.api.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Message {

    @JsonProperty("message_id")
    @SerializedName("message_id")
    public long messageId;

    @JsonProperty("from")
    @SerializedName("from")
    public User from;

    @JsonProperty("forward_from")
    @SerializedName(("forward_from"))
    public User forwardFrom;

    @JsonProperty("forward_sender_name")
    @SerializedName("forward_sender_name")
    public String forwardSenderName;

    @JsonProperty("chat")
    @SerializedName("chat")
    public Chat chat;

    @JsonProperty("date")
    @SerializedName("date")
    public long date;

    @JsonProperty("text")
    @SerializedName("text")
    public String text;

    @JsonProperty("voice")
    @SerializedName("voice")
    public Voice voice;
}
