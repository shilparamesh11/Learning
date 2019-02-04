package MatchEngine.Server.API;

public interface IMatchEngineServer extends ITradeEventGenerator, IOrderChangeEventObserver {
    void match(IMessage aggressiveOrder);
    boolean remove(IMessage aggressiveOrder);
}
