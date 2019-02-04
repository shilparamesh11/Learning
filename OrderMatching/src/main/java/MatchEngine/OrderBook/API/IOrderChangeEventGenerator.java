package MatchEngine.OrderBook.API;

import MatchEngine.Server.API.IOrderChangeEventObserver;

public interface IOrderChangeEventGenerator {
    void register(IOrderChangeEventObserver observer);
}
