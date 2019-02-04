package Server;

import MatchEngine.Client.API.ITradeEventObserver;
import MatchEngine.Client.MessageType;
import MatchEngine.Client.Side;
import MatchEngine.Server.API.IOrder;
import MatchEngine.OrderBook.API.IOrderBook;
import MatchEngine.OrderBook.OrderBook;
import MatchEngine.Server.API.IMatchEngineServer;
import MatchEngine.Server.API.IMessage;
import MatchEngine.Server.API.IMessageBuilder;
import MatchEngine.Server.MatchEngineServer;
import MatchEngine.Server.MessageBuilder;
import MatchEngine.Server.Order;
import MatchEngine.Server.ServerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OrderBook.class)
public class MatchEngineServerTests {
    // Subject
    private IMatchEngineServer matchEngineServer;

    // Data
    @Mock
    IMessage message;
    Calendar calendar = Calendar.getInstance();
    IOrder order;

    // Dependencies
    @Mock
    private IOrderBook orderBook;
    @Mock
    private IMessageBuilder messageBuilder;
    @Mock
    private ITradeEventObserver tradeEventObserver;

    @Before
    public void setUp(){
        messageBuilder = new MessageBuilder();
        orderBook = mock(IOrderBook.class);
        mockStatic(OrderBook.class);
        when(OrderBook.getInstance()).thenReturn(orderBook);
        matchEngineServer = new MatchEngineServer();
        tradeEventObserver = mock(ITradeEventObserver.class);
        order = new Order(calendar.getTime(), 1L, Side.BUY, 1, 15.5);
    }

    @Test
    public void testMatchInvalidMessage(){
        matchEngineServer.match(null);
        verify(orderBook, never()).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testMatchInvalidMessageType(){
        when(message.getMessageType()).thenReturn(MessageType.TRADEEVENT);
        matchEngineServer.match(message);
        verify(orderBook, never()).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testMatchInvalidSide(){
        when(message.getSide()).thenReturn(null);
        matchEngineServer.match(message);
        verify(orderBook, never()).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testMatchInvalidQuantity(){
        when(message.getQuantity()).thenReturn(-1);
        matchEngineServer.match(message);
        verify(orderBook, never()).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testMatchInvalidPrice(){
        when(message.getPrice()).thenReturn(-1.0);
        matchEngineServer.match(message);
        verify(orderBook, never()).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testMatchInvalidOrderId(){
        when(message.getOrderId()).thenReturn(0L);
        matchEngineServer.match(message);
        verify(orderBook, never()).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testValidMessage(){
        when(message.getOrderId()).thenReturn(1L);
        when(message.getPrice()).thenReturn(15.5);
        when(message.getQuantity()).thenReturn(1);
        when(message.getMessageType()).thenReturn(MessageType.ADDORDERREQUEST);
        when(message.getSide()).thenReturn(Side.BUY);
        matchEngineServer.match(message);
        verify(orderBook, times(1)).getBestPriceMatch(any(IOrder.class));
    }

    @Test
    public void testRemoveInvalidMessage(){
        matchEngineServer.remove(null);
        verify(orderBook, never()).removeRestingOrder(any(IOrder.class));
    }

    @Test
    public void testRemoveInvalidMessageType(){
        when(message.getMessageType()).thenReturn(MessageType.ADDORDERREQUEST);
        matchEngineServer.match(message);
        verify(orderBook, never()).removeRestingOrder(any(IOrder.class));
    }

    @Test
    public void testRemoveInvalidOrderId(){
        when(message.getOrderId()).thenReturn(0L);
        matchEngineServer.remove(message);
        verify(orderBook, never()).removeRestingOrder(any(IOrder.class));
    }

    @Test
    public void testValidMessageForRemove(){
        when(message.getOrderId()).thenReturn(1L);
        when(message.getMessageType()).thenReturn(MessageType.CANCELORDERREQUEST);
        matchEngineServer.remove(message);
        verify(orderBook, times(1)).removeRestingOrder(any(IOrder.class));
    }

    @Test
    public void onNextOrderMatch(){
        matchEngineServer.register(tradeEventObserver);
        IMessage expected = messageBuilder.setSide(order.getSide()).setQuantity(order.getQuantity()).setPrice(order.getPrice()).build(MessageType.TRADEEVENT, order.getOrderId());
        matchEngineServer.onNextOrderMatch(order);
        verify(tradeEventObserver, times(1)).onNextTradeEvent(expected);
    }

    @Test
    public void onNextFullOrderMatch(){
        matchEngineServer.register(tradeEventObserver);
        IMessage expected = messageBuilder.setSide(order.getSide()).setQuantity(order.getQuantity()).setPrice(order.getPrice()).build(MessageType.ORDERFULLYFILLED, order.getOrderId());
        matchEngineServer.onNextFullOrderMatch(order);
        verify(tradeEventObserver, times(1)).onNextFullFillEvent(expected);
    }

    @Test
    public void onNextPartialOrderMatch(){
        matchEngineServer.register(tradeEventObserver);
        IMessage expected = messageBuilder.setSide(order.getSide()).setQuantity(order.getQuantity()).setPrice(order.getPrice()).build(MessageType.ORDERPARTIALLYFILLED, order.getOrderId());
        matchEngineServer.onNextPartialOrderMatch(order);
        verify(tradeEventObserver, times(1)).onNextPartialFillEvent(expected);
    }
}
