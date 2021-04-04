package com.example.stocktracking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {
    private String currency;
    private String symbol;
    private double regularMarketPrice;
    private String exchangeTimezoneName;
    private double previousClose;

    public Stock() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getRegularMarketPrice() {
        return regularMarketPrice;
    }

    public void setRegularMarketPrice(double regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
    }

    public String getExchangeTimezoneName() {
        return exchangeTimezoneName;
    }

    public void setExchangeTimezoneName(String exchangeTimezoneName) {
        this.exchangeTimezoneName = exchangeTimezoneName;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "currency='" + currency + '\'' +
                ", symbol='" + symbol + '\'' +
                ", regularMarketPrice=" + regularMarketPrice +
                ", exchangeTimezoneName='" + exchangeTimezoneName + '\'' +
                ", previousClose=" + previousClose +
                '}';
    }
}
