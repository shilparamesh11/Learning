package MatchEngine.Server;

import MatchEngine.Server.API.IMessage;
import MatchEngine.Server.API.IMessageBuilder;
import MatchEngine.Client.MessageType;
import MatchEngine.Client.Side;

public final class MessageBuilder implements IMessageBuilder {
    private MessageType messageType;
    private long orderId;
    private Side side;
    private int quantity;
    private double price;

    public MessageBuilder setSide(Side side){
        this.side = side;
        return this;
    }

    public MessageBuilder setQuantity(int quantity){
        this.quantity = quantity;
        return this;
    }

    public MessageBuilder setPrice(double price){
        this.price = price;
        return this;
    }

    public IMessage build(MessageType messageType, long orderId){
        this.messageType = messageType;
        this.orderId = orderId;

        if(messageType == null) throw new IllegalArgumentException("Invalid message type");
        if(orderId <= 0) throw new IllegalArgumentException("Order Id should be a positive integer");
        switch(this.messageType){
            case ADDORDERREQUEST:
                if(side == null) throw new IllegalArgumentException("Invalid order side");
                if(price <= 0) throw new IllegalArgumentException("Price should be positive");
                if(quantity <= 0) throw new IllegalArgumentException("Order quantity cannot be 0");
            case TRADEEVENT:
                if(price <= 0) throw new IllegalArgumentException("Price should be positive");
                if(quantity <= 0) throw new IllegalArgumentException("Order quantity cannot be 0");
            case ORDERPARTIALLYFILLED:
                if(quantity <= 0)  throw new IllegalArgumentException("Order Id should be non-zero positive integer");
        }
        return new Message();
    }

    private class Message implements IMessage{
        private final MessageType messageType;
        private final long orderId;
        private final Side side;
        private final int quantity;
        private final double price;

        private Message() {
            this.messageType = MessageBuilder.this.messageType;
            this.orderId = MessageBuilder.this.orderId;
            this.side = MessageBuilder.this.side;
            this.quantity = MessageBuilder.this.quantity;
            this.price = MessageBuilder.this.price;
        }

        public MessageType getMessageType(){return this.messageType;}

        public long getOrderId(){return this.orderId;}

        public Side getSide(){return this.side;}

        public int getQuantity(){return this.quantity;}

        public double getPrice(){return this.price;}

        @Override
        public boolean equals(Object obj){
            if(obj instanceof IMessage) return equals((IMessage) obj);
            return false;
        }

        private boolean equals(IMessage message){
            if(messageType == message.getMessageType() && orderId == message.getOrderId()) return true;
            return false;
        }

        @Override
        public int hashCode(){
            return messageType.hashCode() ^ Long.hashCode(orderId);
        }

        public String toString() {
            switch (messageType) {
                case ADDORDERREQUEST:
                    return messageType.toInteger() + "," + this.orderId + "," + this.side.toInteger() + "," + this.quantity + "," + this.price;
                case CANCELORDERREQUEST:
                    return messageType.toInteger() + "," + this.orderId;
                case TRADEEVENT:
                    return messageType.toInteger() + "," + this.quantity + "," + this.price;
                case ORDERPARTIALLYFILLED:
                    return messageType.toInteger() + "," + this.orderId + "," + this.side.toInteger() + "," + this.quantity;
                case ORDERFULLYFILLED:
                    return messageType.toInteger() + "," + this.orderId;
            }
            return "MessageType<" + this.messageType + "> OrderId<" + this.orderId + "> MatchEngine.Client.Side<" + this.side + "> Quantity<" + this.quantity + "> Price<" + this.price + ">";
        }
    }
}
