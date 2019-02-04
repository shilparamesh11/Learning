package MatchEngine.Server.API;

import MatchEngine.Client.MessageType;
import MatchEngine.Client.Side;

public interface IMessageBuilder {
    IMessageBuilder setQuantity(int quantity);
    IMessageBuilder setPrice(double price);
    IMessageBuilder setSide(Side side);
    IMessage build(MessageType messageType, long orderId);
}
