package MatchEngine.Server;

import MatchEngine.OrderBook.OrderBook;
import MatchEngine.Server.API.IMessage;
import MatchEngine.Server.API.IMessageBuilder;
import MatchEngine.Client.API.ITradeEventObserver;
import MatchEngine.Client.MessageType;
import MatchEngine.Client.Side;
import MatchEngine.Server.API.IOrder;
import MatchEngine.OrderBook.API.IOrderBook;
import MatchEngine.Server.API.IMatchEngineServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Handles requests from client
public class MatchEngineServer implements IMatchEngineServer {
    private final IOrderBook orderBook;
    private final List<ITradeEventObserver> observers;
    private final Calendar calender;
    private final IMessageBuilder messageBuilder;

    public MatchEngineServer(){
        orderBook = OrderBook.getInstance();
        orderBook.register(this);
        observers = new ArrayList<>();
        calender = Calendar.getInstance();
        messageBuilder = ServerFactory.getMessageBuilder();
    }

    @Override
    public void match(IMessage message){
        if(isNotValidMatchMessage(message)) return;

        System.out.println("Received request from client to add : " + message.toString());
        IOrder aggressiveOrder = toOrder(message);

        System.out.println("Getting best price for order : " + aggressiveOrder.toString());
        orderBook.getBestPriceMatch(aggressiveOrder);
    }

    @Override
    public boolean remove(IMessage message){
        if(isNotValidRemoveMessage(message)) return false;

        System.out.println("Received request from client to remove : " + message.toString());
        IOrder aggressiveOrder = toOrder(message);

        System.out.println("Removing order : " + aggressiveOrder.toString());
        return orderBook.removeRestingOrder(aggressiveOrder);
    }

    @Override
    public void register(ITradeEventObserver observer){
        observers.add(observer);
    }

    @Override
    public void onNextOrderMatch(IOrder order) {
        IMessage message = toMessage(MessageType.TRADEEVENT, order);

        for (ITradeEventObserver observer: observers) {
            observer.onNextTradeEvent(message);
        }
    }

    @Override
    public void onNextFullOrderMatch(IOrder order) {
        IMessage message = toMessage(MessageType.ORDERFULLYFILLED, order);

        for (ITradeEventObserver observer: observers) {
            observer.onNextFullFillEvent(message);
        }
    }

    @Override
    public void onNextPartialOrderMatch(IOrder order) {
        IMessage message = toMessage(MessageType.ORDERPARTIALLYFILLED, order);

        for (ITradeEventObserver observer: observers) {
            observer.onNextPartialFillEvent(message);
        }
    }

    private IMessage toMessage(MessageType messageType, IOrder input){
        return messageBuilder
                .setSide(input.getSide())
                .setQuantity(input.getQuantity())
                .setPrice(input.getPrice())
                .build(messageType, input.getOrderId());
    }

    private IOrder toOrder(IMessage input){
        return new Order(calender.getTime(), input.getOrderId(), input.getSide(), input.getQuantity(), input.getPrice());
    }

    private boolean isNotValidMatchMessage(IMessage message) {
        return message == null ||
                message.getMessageType() != MessageType.ADDORDERREQUEST ||
                message.getOrderId() <= 0 ||
                message.getQuantity() <= 0 ||
                (message.getSide() != Side.SELL && message.getSide() != Side.BUY) ||
                message.getPrice() <= 0;
    }

    private boolean isNotValidRemoveMessage(IMessage message) {
        return message == null ||
                message.getMessageType() != MessageType.CANCELORDERREQUEST ||
                message.getOrderId() <= 0;
    }
}
