package Client;

import MatchEngine.Server.API.IMessage;
import MatchEngine.Client.MatchEngineClient;
import MatchEngine.Client.MessageType;
import MatchEngine.Server.API.IMatchEngineServer;
import MatchEngine.Server.MatchEngineServer;
import MatchEngine.Server.ServerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServerFactory.class)
public class MatchEngineClientTests {
    // Subject
    private MatchEngineClient matchEngineClient;

    // Data
    @Mock
    private IMessage message;

    // Dependencies
    @Mock
    private IMatchEngineServer matchEngineServer;

    @Before
    public void setUp(){
        matchEngineServer = mock(MatchEngineServer.class);
        mockStatic(ServerFactory.class);
        when(ServerFactory.getMatchEngineServer()).thenReturn(matchEngineServer);
        matchEngineClient = new MatchEngineClient();
        message = mock(IMessage.class);
    }

    @Test
    public void testMatchOrderForAddRequest(){
        when(message.getMessageType()).thenReturn(MessageType.ADDORDERREQUEST);
        matchEngineClient.matchOrder(message);
        verify(matchEngineServer, times(1)).match(message);
    }

    @Test
    public void testMatchOrderForCancelRequest(){
        when(message.getMessageType()).thenReturn(MessageType.CANCELORDERREQUEST);
        matchEngineClient.matchOrder(message);
        verify(matchEngineServer, never()).match(message);
    }

    @Test
    public void testRemoveOrderForCancelRequest(){
        when(message.getMessageType()).thenReturn(MessageType.CANCELORDERREQUEST);
        matchEngineClient.removeOrder(message);
        verify(matchEngineServer, times(1)).remove(message);
    }

    @Test
    public void testRemoveOrderForTradeEvent(){
        when(message.getMessageType()).thenReturn(MessageType.TRADEEVENT);
        matchEngineClient.removeOrder(message);
        verify(matchEngineServer, never()).remove(message);
    }

    @Test
    public void testOnNextTradeEvent(){
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actual));
        String expectedToString = "Trade Event";

        when(message.toString()).thenReturn(expectedToString);
        matchEngineClient.onNextTradeEvent(message);
        Assert.assertEquals(expectedToString.trim(), actual.toString().trim());
    }

    @Test
    public void testOnNextPartialFillEvent(){
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actual));
        String expectedToString = "Partial Fill Event";

        when(message.toString()).thenReturn(expectedToString);
        matchEngineClient.onNextPartialFillEvent(message);
        Assert.assertEquals(expectedToString.trim(), actual.toString().trim());
    }

    @Test
    public void testOnNextFullFillEvent(){
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        System.setOut(new PrintStream(actual));
        String expectedToString = "Full Fill Event";

        when(message.toString()).thenReturn(expectedToString);
        matchEngineClient.onNextFullFillEvent(message);
        Assert.assertEquals(expectedToString.trim(), actual.toString().trim());
    }
}
