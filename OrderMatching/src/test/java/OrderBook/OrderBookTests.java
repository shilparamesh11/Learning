package OrderBook;

import MatchEngine.Client.Side;
import MatchEngine.OrderBook.API.IOrderBook;
import MatchEngine.OrderBook.OrderBook;
import MatchEngine.Server.API.IOrder;
import MatchEngine.Server.API.IOrderChangeEventObserver;
import MatchEngine.Server.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IOrderBook.class)
public class OrderBookTests {
    // Subject
    private IOrderBook orderBook;

    // Data
    private IOrder buyOrder, sellOrder;
    private Calendar calendar = Calendar.getInstance();
    private Date createdOn;
    private long orderId;
    private int quantity;
    private double price;
    private IOrderChangeEventObserver orderChangeEventObserver;

    @Before
    public void setUp(){
        orderBook = OrderBook.getInstance();
        orderBook.removeRestingOrder(sellOrder);
        orderBook.removeRestingOrder(buyOrder);

        createdOn = calendar.getTime();
        orderId = 1L;
        quantity = 5;
        price = 10;

        orderChangeEventObserver = mock(IOrderChangeEventObserver.class);
        orderBook.register(orderChangeEventObserver);
    }

    @After
    public void cleanUp(){
        orderBook.removeRestingOrder(sellOrder);
        orderBook.removeRestingOrder(buyOrder);
    }

    @Test
    public void testAddNewBuyOrder(){
        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        boolean removed = orderBook.removeRestingOrder(buyOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testAddNewSellOrder(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        boolean removed = orderBook.removeRestingOrder(sellOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testMatchSellOrderWithSamePrice(){
        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextFullOrderMatch(any());
    }

    @Test
    public void testMatchBuyOrderWithSamePrice(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextFullOrderMatch(any());
    }

    @Test
    public void testMatchSellOrderWithLesserPrice(){
        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price / 2.0);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextFullOrderMatch(any());
    }

    @Test
    public void testMatchBuyOrderWithGreaterPrice(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, 2 * price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextFullOrderMatch(any());
    }

    @Test
    public void testPartialMatchBuyAggressiveOrder(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        buyOrder = new Order(createdOn, orderId, Side.BUY, 2 * quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(1)).onNextFullOrderMatch(sellOrder);
        verify(orderChangeEventObserver, times(1)).onNextPartialOrderMatch(buyOrder);

        boolean removed = orderBook.removeRestingOrder(sellOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testPartialMatchSellAggressiveOrder(){
        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        sellOrder = new Order(createdOn, orderId, Side.SELL, 2 * quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(1)).onNextFullOrderMatch(buyOrder);
        verify(orderChangeEventObserver, times(1)).onNextPartialOrderMatch(sellOrder);

        boolean removed = orderBook.removeRestingOrder(sellOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testPartialMatchSellRestingOrder(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, 2 * quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        buyOrder = new Order(createdOn, orderId, Side.BUY, quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(1)).onNextFullOrderMatch(buyOrder);
        verify(orderChangeEventObserver, times(1)).onNextPartialOrderMatch(sellOrder);

        boolean removed = orderBook.removeRestingOrder(sellOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testPartialMatchBuyRestingOrder(){
        buyOrder = new Order(createdOn, orderId, Side.BUY, 2 * quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, times(1)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(1)).onNextFullOrderMatch(buyOrder);
        verify(orderChangeEventObserver, times(1)).onNextPartialOrderMatch(sellOrder);

        boolean removed = orderBook.removeRestingOrder(buyOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testBuyOrderMatchWithSamePrices(){
        sellOrder = new Order(createdOn, 1L, Side.SELL, 2 * quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        sellOrder = new Order(calendar.getTime(), 2L, Side.SELL, 2 * quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        buyOrder = new Order(createdOn, orderId, Side.BUY, 3 * quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, times(2)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextPartialOrderMatch(any());

        boolean removed = orderBook.removeRestingOrder(sellOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testSellOrderMatchWithSamePrices(){
        buyOrder = new Order(createdOn, 1L, Side.BUY, 2 * quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        buyOrder = new Order(calendar.getTime(), 2L, Side.BUY, 2 * quantity, price);

        orderBook.getBestPriceMatch(buyOrder);

        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());

        IOrder sellOrder = new Order(createdOn, orderId, Side.SELL, 3 * quantity, price);

        orderBook.getBestPriceMatch(sellOrder);

        verify(orderChangeEventObserver, times(2)).onNextOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, times(2)).onNextPartialOrderMatch(any());

        boolean removed = orderBook.removeRestingOrder(buyOrder);
        Assert.assertTrue(removed);
    }

    @Test
    public void testRemoveBuyOrderNotPresent(){
        buyOrder = new Order(createdOn, 1L, Side.BUY, 2 * quantity, price);
        orderBook.removeRestingOrder(buyOrder);
        boolean removed = orderBook.removeRestingOrder(buyOrder);
        Assert.assertFalse(removed);
    }

    @Test
    public void testRemoveSellOrderNotPresent(){
        sellOrder = new Order(createdOn, 1L, Side.SELL, 2 * quantity, price);
        boolean removed = orderBook.removeRestingOrder(sellOrder);
        Assert.assertFalse(removed);
    }

    @Test
    public void testRemoveInvalidOrder(){
        orderBook.getBestPriceMatch(null);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }

    @Test
    public void testRemoveInvalidOrderId(){
        sellOrder = new Order(createdOn, 1L, Side.SELL, 2 * quantity, price);
        orderBook.getBestPriceMatch(sellOrder);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }

    @Test
    public void testMatchInvalidOrder(){
        orderBook.getBestPriceMatch(null);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }

    @Test
    public void testMatchInvalidSide(){
        sellOrder = new Order(createdOn, orderId, null, quantity, price);
        orderBook.getBestPriceMatch(sellOrder);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }

    @Test
    public void testMatchInvalidQuantity(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, -1, price);
        orderBook.getBestPriceMatch(sellOrder);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }

    @Test
    public void testMatchInvalidPrice(){
        sellOrder = new Order(createdOn, orderId, Side.SELL, quantity, -100);
        orderBook.getBestPriceMatch(sellOrder);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }

    @Test
    public void testMatchInvalidOrderId(){
        sellOrder = new Order(createdOn, 0, Side.SELL, -1, price);
        orderBook.getBestPriceMatch(sellOrder);
        verify(orderChangeEventObserver, never()).onNextOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextFullOrderMatch(any());
        verify(orderChangeEventObserver, never()).onNextPartialOrderMatch(any());
    }
}