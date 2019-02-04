package MatchEngine.Server;

import MatchEngine.Server.API.IMatchEngineServer;
import MatchEngine.Server.API.IMessageBuilder;

public class ServerFactory {

    public static IMatchEngineServer getMatchEngineServer(){
        return new MatchEngineServer();
    }

    public static IMessageBuilder getMessageBuilder(){
        return new MessageBuilder();
    }
}
