package com.github.bablo_org.bablo_project.api.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class FileInfo {

    @JsonProperty("file_id")
    @SerializedName("file_id")
    public String fileId;

    @JsonProperty("file_unique_id")
    @SerializedName("file_unique_id")
    public String fileUniqueId;

    @JsonProperty("file_size")
    @SerializedName("file_size")
    public long fileSize;

    @JsonProperty("file_path")
    @SerializedName("file_path")
    public String filePath;
}
