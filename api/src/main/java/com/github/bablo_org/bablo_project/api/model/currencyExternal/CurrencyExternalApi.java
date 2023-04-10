package com.github.bablo_org.bablo_project.api.model.currencyExternal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CurrencyExternalApi {

    public String result;

    public String documentation;

    @JsonProperty("terms_of_use")
    @SerializedName("terms_of_use")
    public String termsOfUse;

    @JsonProperty("time_last_update_unix")
    @SerializedName("time_last_update_unix")
    public Date timeLastUpdateUnix;

    @JsonProperty("time_last_update_utc")
    @SerializedName("time_last_update_utc")
    public String timeLastUpdateUtc;

    @JsonProperty("time_next_update_unix")
    @SerializedName("time_next_update_unix")
    public Date timeNextUpdateUnix;

    @JsonProperty("time_next_update_utc")
    @SerializedName("time_next_update_utc")
    public String timeNextUpdateUtc;

    @JsonProperty("base_code")
    @SerializedName("base_code")
    public String baseCode;

    @JsonProperty("conversion_rates")
    @SerializedName("conversion_rates")
    public ConversionRates conversionRates;
}
