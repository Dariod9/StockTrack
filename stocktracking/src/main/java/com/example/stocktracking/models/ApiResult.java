package com.example.stocktracking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResult {
    private String region;
    private String quoteType;

    public ApiResult() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "region='" + region + '\'' +
                ", quoteType='" + quoteType + '\'' +
                '}';
    }
}
