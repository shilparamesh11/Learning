package MatchEngine.Server;

import MatchEngine.Server.API.IOrder;
import MatchEngine.Client.Side;

import java.util.Date;

public final class Order implements IOrder {
    private final Date createdOn;
    private final long orderId;
    private final Side side;
    private int quantity;
    private final double price;

    public Order(Date createdOn, long orderId, Side side, int quantity, double price){
        this.createdOn = createdOn;
        this.orderId = orderId;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public long getOrderId() {
        return orderId;
    }

    public Side getSide() {
        return side;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public IOrder copy(){
        return new Order(createdOn, orderId, side, quantity, price);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(obj instanceof Order) return equals((Order) obj);
        else return false;
    }

    private boolean equals(Order order){
        return this.orderId == order.getOrderId();
    }

    @Override
    public int hashCode(){
        return Long.hashCode(this.orderId);
    }

    public String toString(){
        return "CreatedOn<" + createdOn + "> OrderId<" + orderId + "> Side<" + side + "> Quantity<" + quantity + "> Price<" + price + ">";
    }
}
