package MatchEngine.Client;

import MatchEngine.Server.API.IMatchEngineServer;
import MatchEngine.Server.API.IMessage;
import MatchEngine.Client.API.ITradeEventObserver;
import MatchEngine.Server.ServerFactory;

// This class acts as a means to interact with match engine.
// It also listens to the trade events produced by the match engine.
public class MatchEngineClient implements ITradeEventObserver{
    private final IMatchEngineServer matchEngineServer;

    public MatchEngineClient(){
        matchEngineServer = ServerFactory.getMatchEngineServer();
        matchEngineServer.register(this);
    }

    public void matchOrder(IMessage message){
        if (message.getMessageType() == MessageType.ADDORDERREQUEST){
            System.out.println("Sending request to match engine for adding order");
            matchEngineServer.match(message);
        } else {
            System.out.println("Not a valid input type to match..");
        }
    }

    public void removeOrder(IMessage message){
        if(message.getMessageType() == MessageType.CANCELORDERREQUEST) {
            System.out.println("Sending request to match engine for removing order");
            matchEngineServer.remove(message);
        }
        else {
            System.out.println("Not a valid input type to remove..");
        }
    }

    @Override
    public void onNextTradeEvent(IMessage message) {
        System.out.println(message);
    }

    @Override
    public void onNextFullFillEvent(IMessage message) {
        System.out.println(message);
    }

    @Override
    public void onNextPartialFillEvent(IMessage message) {
        System.out.println(message);
    }
}
