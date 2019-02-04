package MatchEngine.Client.API;

import MatchEngine.Server.API.IMessage;

public interface ITradeEventObserver {
    void onNextTradeEvent(IMessage message);
    void onNextPartialFillEvent(IMessage message);
    void onNextFullFillEvent(IMessage message);
}
