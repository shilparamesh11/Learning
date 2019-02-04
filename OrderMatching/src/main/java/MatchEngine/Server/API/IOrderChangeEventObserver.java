package MatchEngine.Server.API;

public interface IOrderChangeEventObserver {
    void onNextOrderMatch(IOrder order);
    void onNextPartialOrderMatch(IOrder order);
    void onNextFullOrderMatch(IOrder order);
}
