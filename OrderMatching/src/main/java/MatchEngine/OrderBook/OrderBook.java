package MatchEngine.OrderBook;

import MatchEngine.Client.Side;
import MatchEngine.Server.API.IOrder;
import MatchEngine.OrderBook.API.IOrderBook;
import MatchEngine.Server.API.IOrderChangeEventObserver;

import java.util.*;

// Maintains prices and arrival order in priority queue.
// Priority queue internally stores orders in binary tree
// Priority queue offers remove and adding element in log(n) time, remove(element) in linear time and peek in constant time
// This data structure helps solve the problem in efficient manner where lookup of resting order is required for every aggressive order.
// Also because the nature of the data is required to be ordered by price and then date.

// Other data structure I weighed upon before using priority queue are as below:
// Linked list = Takes linear time complexity for inserting the element in sorted order.
// Stack = Does not suit the nature of data.
// Arrays = Does not maintain the order of elements. Searching for 1 resting order every time aggressive order arrives takes log(n).
// Therefore n * log(n) time complexity for n searches of resting order with best price.
// Tree Map = Provides same time complexity as priority queue. However consumes additional memory for storing keys which is not required for this problem.
public class OrderBook implements IOrderBook {
    private final PriorityQueue<IOrder> buys;
    private final PriorityQueue<IOrder> sells;
    private static IOrderBook instance;
    private final List<IOrderChangeEventObserver> observers;

    private OrderBook(){
        observers = new ArrayList<>();

        // Comparator for sorting buy orders on price in ascending order for buys and then by date ascending
        buys = new PriorityQueue<>((Comparator.comparing(IOrder::getPrice).reversed()).thenComparing(IOrder::getCreatedOn));

        // Comparator for sorting them on price in descending order for sells and then by date ascending
        sells = new PriorityQueue<>(Comparator.comparing(IOrder::getPrice).thenComparing(IOrder::getCreatedOn));
    }

    // Singleton to maintain one order book across many server
    public static IOrderBook getInstance(){
        if(instance == null) instance = new OrderBook();
        return instance;
    }

    @Override
    public boolean removeRestingOrder(IOrder restingOrder){
        if(isNotValidRemoveOrder(restingOrder)) return false;
        boolean removed = buys.remove(restingOrder);
        if (!removed) removed = sells.remove(restingOrder);
        System.out.println("Order removed : " + removed);
        return removed;
    }

    @Override
    public void getBestPriceMatch(IOrder aggressiveOrder)
    {
        if(isNotValidMatchOrder(aggressiveOrder)) return;
        IOrder restingOrder;
        Side side;

        int aggressiveOrderQuantity = aggressiveOrder.getQuantity();
        while(aggressiveOrderQuantity > 0){
            if (aggressiveOrder.getSide() == Side.BUY) {
                side = Side.SELL;
                restingOrder = peekRestingOrder(side);
                if(restingOrder == null || restingOrder.getPrice() > aggressiveOrder.getPrice()) break;
            } else {
                side = Side.BUY;
                restingOrder = peekRestingOrder(side);
                if(restingOrder == null || restingOrder.getPrice() < aggressiveOrder.getPrice()) break;
            }
            System.out.println("Order Book found a match for the order");
            aggressiveOrderQuantity -= restingOrder.getQuantity();
            adjustOrderBook(aggressiveOrder, restingOrder);
        }

        if(aggressiveOrderQuantity > 0){
            System.out.println("Could not find best price for " + aggressiveOrder.toString());
            addRestingOrder(aggressiveOrder);
        }
    }

    private boolean isNotValidMatchOrder(IOrder restingOrder) {
        return restingOrder == null ||
                restingOrder.getOrderId() <= 0 ||
                (restingOrder.getSide() != Side.BUY && restingOrder.getSide() != Side.SELL) ||
                restingOrder.getPrice() <= 0 ||
                restingOrder.getQuantity() <= 0;
    }

    private boolean isNotValidRemoveOrder(IOrder restingOrder) {
        return restingOrder == null || restingOrder.getOrderId() <= 0;
    }

    private void addRestingOrder(IOrder restingOrder){
        if (restingOrder.getSide() == Side.BUY)
            buys.add(restingOrder);
        else
            sells.add(restingOrder);
        System.out.println("Order added to Resting orders");
    }

    private void adjustOrderBook(IOrder aggressiveOrder, IOrder restingOrder){
        // generate trade event with lesser quantity between the aggressive order and resting order with price of resting order
        int minQuantity = aggressiveOrder.getQuantity();
        if (minQuantity > restingOrder.getQuantity()) minQuantity = restingOrder.getQuantity();
        IOrder order = restingOrder.copy();
        order.setQuantity(minQuantity);
        sendTradeEvent(order);

        if (restingOrder.getQuantity() > aggressiveOrder.getQuantity()) {
            restingOrder.setQuantity(restingOrder.getQuantity() - aggressiveOrder.getQuantity());
            sendOrderFulfilledEvent(aggressiveOrder);
            sendPartialOrderFulfilledEvent(restingOrder);
        } else if (restingOrder.getQuantity() == aggressiveOrder.getQuantity()) {
            sendOrderFulfilledEvent(aggressiveOrder);
            sendOrderFulfilledEvent(restingOrder);
            removeRestingOrder(restingOrder.getSide());
        } else {
            aggressiveOrder.setQuantity(aggressiveOrder.getQuantity() - restingOrder.getQuantity());
            sendPartialOrderFulfilledEvent(aggressiveOrder);
            sendOrderFulfilledEvent(restingOrder);
            removeRestingOrder(restingOrder.getSide());
        }
    }

    private void sendTradeEvent(IOrder order) {
        for (IOrderChangeEventObserver observer: observers) {
            observer.onNextOrderMatch(order);
        }
    }

    private void sendOrderFulfilledEvent(IOrder order) {
        for (IOrderChangeEventObserver observer: observers) {
            observer.onNextFullOrderMatch(order);
        }
    }

    private void sendPartialOrderFulfilledEvent(IOrder order) {
        for (IOrderChangeEventObserver observer: observers) {
            observer.onNextPartialOrderMatch(order);
        }
    }

    private IOrder removeRestingOrder(Side side){
        if (side == Side.BUY && !buys.isEmpty())
            return buys.remove();
        else if (side == Side.SELL && !sells.isEmpty())
            return sells.remove();
        else return null;
    }

    private IOrder peekRestingOrder(Side side){
        if (side == Side.BUY)
            return buys.peek();
        else
            return sells.peek();
    }

    @Override
    public void register(IOrderChangeEventObserver observer) {
        observers.add(observer);
    }
}
