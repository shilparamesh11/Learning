package MatchEngine.Server.API;

import MatchEngine.Client.Side;
import MatchEngine.Client.MessageType;

public interface IMessage {
    MessageType getMessageType();
    long getOrderId();
    Side getSide();
    int getQuantity();
    double getPrice();
}
