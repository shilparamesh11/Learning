package MatchEngine.Server.API;

import MatchEngine.Client.Side;

import java.util.Date;

public interface IOrder {
    Date getCreatedOn();
    long getOrderId();
    Side getSide();
    int getQuantity();
    void setQuantity(int quantity);
    double getPrice();
    IOrder copy();
}
