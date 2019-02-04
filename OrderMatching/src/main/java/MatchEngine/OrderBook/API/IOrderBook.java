package MatchEngine.OrderBook.API;

import MatchEngine.Server.API.IOrder;

public interface IOrderBook extends IOrderChangeEventGenerator {
    boolean removeRestingOrder(IOrder restingOrder);
    void getBestPriceMatch(IOrder aggressiveOrder);
}
