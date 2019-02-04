package Server;

import MatchEngine.Server.API.IMessage;
import MatchEngine.Server.API.IOrder;
import MatchEngine.Server.MessageBuilder;
import MatchEngine.Client.MessageType;
import MatchEngine.Client.Side;
import MatchEngine.Server.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;

public class MessageTests {
    // subject
    private IMessage message;

    // dependencies
    private MessageBuilder messageBuilder;

    // data
    private MessageType messageType;
    private long orderId;
    private Side side;
    private int quantity;
    private double price;

    @Before
    public void setUp(){
        messageType = MessageType.ADDORDERREQUEST;
        orderId = 1;
        side = Side.BUY;
        quantity = 1;
        price = 20.0050;

        messageBuilder = new MessageBuilder();
    }

    @Test
    public void createMessageSuccessfully(){
        message = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        Assert.assertEquals(messageType, message.getMessageType());
        Assert.assertEquals(orderId, message.getOrderId());
        Assert.assertEquals(side, message.getSide());
        Assert.assertEquals(quantity, message.getQuantity());
        Assert.assertEquals(price, message.getPrice(), Double.NaN);
    }

    @Test
    public void toStringMessageType0(){
        message = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        String expected = messageType.toInteger() + "," + orderId + "," + side.toInteger() + "," + quantity + "," + price;
        Assert.assertEquals(expected, message.toString());
    }

    @Test
    public void toStringMessageType1(){
        messageType = MessageType.CANCELORDERREQUEST;
        message = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        String expected = messageType.toInteger() + "," + orderId;
        Assert.assertEquals(expected, message.toString());
    }

    @Test
    public void toStringMessageType2(){
        messageType = MessageType.TRADEEVENT;
        message = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        String expected = messageType.toInteger() + "," + quantity + "," + price;
        Assert.assertEquals(expected, message.toString());
    }

    @Test
    public void toStringMessageType3(){
        messageType = MessageType.ORDERPARTIALLYFILLED;
        message = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        String expected = messageType.toInteger() + "," + orderId + "," + side.toInteger() + "," + quantity;
        Assert.assertEquals(expected, message.toString());
    }

    @Test
    public void testEqualsTo(){
        messageType = MessageType.ORDERPARTIALLYFILLED;
       IMessage message1 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        IMessage message2 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        Assert.assertTrue(message1.equals(message2));
        Assert.assertTrue(message2.equals(message1));

        message2 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(MessageType.TRADEEVENT, orderId);
        Assert.assertFalse(message1.equals(message2));
        Assert.assertFalse(message2.equals(message1));

        message2 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, 15L);
        Assert.assertFalse(message1.equals(message2));
        Assert.assertFalse(message2.equals(message1));

        Object obj = new Object();
        Assert.assertFalse(message1.equals(obj));
    }

    @Test
    public void testHashCode(){
        IMessage message1 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        IMessage message2 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, orderId);
        HashMap<IMessage, Integer> messageList = new HashMap<>();
        messageList.put(message1, 0);
        messageList.put(message2, 1);

        Assert.assertEquals(messageList.size(), 1);
        Assert.assertTrue(messageList.get(message1) == 1);

        IMessage message3 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(MessageType.TRADEEVENT, orderId);
        messageList.put(message3, 2);

        IMessage message4 = messageBuilder
                .setSide(side)
                .setQuantity(quantity)
                .setPrice(price)
                .build(messageType, 100L);
        messageList.put(message4, 3);

        Assert.assertEquals(messageList.size(), 3);
    }
}
