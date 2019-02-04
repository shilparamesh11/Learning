package Server;

import MatchEngine.Client.Side;
import MatchEngine.Server.API.IOrder;
import MatchEngine.Server.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OrderTests {
    // Subject
    private Order order;

    // Data
    private Date time;
    private long orderId;
    private Side side;
    private int quantity;
    private double price;

    // Dependencies
    private Calendar calendar = Calendar.getInstance();

    @Before
    public void setUp(){
        time = calendar.getTime();
        orderId = 1L;
        side = Side.BUY;
        quantity = 1;
        price = 100;

        order = new Order(time, orderId, side, quantity, price);
    }

    @Test
    public void testCreatingOrder(){
        Assert.assertEquals(time, order.getCreatedOn());
        Assert.assertEquals(orderId, order.getOrderId());
        Assert.assertEquals(side, order.getSide());
        Assert.assertEquals(quantity, order.getQuantity());
        Assert.assertEquals(price, order.getPrice(), Double.NaN);
    }

    @Test
    public void testCopy(){
        IOrder copy = order.copy();
        Assert.assertEquals(time, copy.getCreatedOn());
        Assert.assertEquals(orderId, copy.getOrderId());
        Assert.assertEquals(side, copy.getSide());
        Assert.assertEquals(quantity, copy.getQuantity());
        Assert.assertEquals(price, copy.getPrice(), Double.NaN);
    }

    @Test
    public void testEqualsTo(){
        IOrder order1 = order.copy();
        Assert.assertTrue(order.equals(order1));
        Assert.assertTrue(order1.equals(order));

        order1 = new Order(calendar.getTime(), 2L, side, quantity, price);
        Assert.assertFalse(order.equals(order1));
        Assert.assertFalse(order1.equals(order));

        Object obj = new Object();
        Assert.assertFalse(order.equals(obj));
    }

    @Test
    public void testHashCode(){
        HashMap<IOrder, Integer> orderList = new HashMap<>();
        orderList.put(order, 0);
        orderList.put(order.copy(), 1);

        Assert.assertTrue(orderList.size() == 1);
        Assert.assertTrue(orderList.get(order) == 1);

        IOrder order1 = new Order(calendar.getTime(), 2L, side, quantity, price);
        orderList.put(order1, 2);
        Assert.assertTrue(orderList.size() == 2);
        Assert.assertTrue(orderList.get(order1) == 2);
    }

    @Test
    public void testToString(){
        String actual = order.toString().trim();
        String expected = "CreatedOn<"+ time +"> OrderId<1> Side<BUY> Quantity<1> Price<100.0>";
        Assert.assertEquals(actual, expected);
    }
}
