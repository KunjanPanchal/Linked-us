package com.example.linkedus.Model;

public class Order {

    private  String cusId, feedId;

    public Order() {
    }

    public Order(String cusId, String feedId) {
        this.cusId = cusId;
        this.feedId = feedId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }
}
