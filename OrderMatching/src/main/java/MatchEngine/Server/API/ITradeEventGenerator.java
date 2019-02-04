package MatchEngine.Server.API;

import MatchEngine.Client.API.ITradeEventObserver;

public interface ITradeEventGenerator {
    void register(ITradeEventObserver observer);
}
