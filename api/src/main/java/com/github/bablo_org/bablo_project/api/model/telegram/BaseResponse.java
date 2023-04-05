package com.github.bablo_org.bablo_project.api.model.telegram;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("ok")
    public boolean ok;

    @SerializedName("result")
    public T result;
}
