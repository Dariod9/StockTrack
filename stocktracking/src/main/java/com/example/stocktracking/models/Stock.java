package com.example.stocktracking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {

    @JsonProperty("01. symbol")
    public String symbol;
    @JsonProperty("05. price")
    private double price;
    @JsonProperty("08. previous close")
    private double previousClose;
    @JsonProperty("10. change percent")
    private String percent;


    public Stock() {
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                ", previousClose=" + previousClose +
                ", percent='" + percent + '\'' +
                '}';
    }
}
