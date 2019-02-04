package Client;

import MatchEngine.Client.MessageType;
import org.junit.Assert;
import org.junit.Test;

public class MessageTypeTests {

    @Test
    public void testToInteger(){
        Assert.assertEquals(0, MessageType.ADDORDERREQUEST.toInteger());
        Assert.assertEquals(1, MessageType.CANCELORDERREQUEST.toInteger());
        Assert.assertEquals(2, MessageType.TRADEEVENT.toInteger());
        Assert.assertEquals(3, MessageType.ORDERFULLYFILLED.toInteger());
        Assert.assertEquals(4, MessageType.ORDERPARTIALLYFILLED.toInteger());
    }

    @Test
    public void testToMessageType(){
        Assert.assertEquals(MessageType.toMessageType(0), MessageType.ADDORDERREQUEST);
        Assert.assertEquals(MessageType.toMessageType(1), MessageType.CANCELORDERREQUEST);
        Assert.assertEquals(MessageType.toMessageType(2), MessageType.TRADEEVENT);
        Assert.assertEquals(MessageType.toMessageType(3), MessageType.ORDERFULLYFILLED);
        Assert.assertEquals(MessageType.toMessageType(4), MessageType.ORDERPARTIALLYFILLED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMessageType(){
        MessageType.toMessageType(18);
    }
}
