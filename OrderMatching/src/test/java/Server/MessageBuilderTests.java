package Server;

import MatchEngine.Server.MessageBuilder;
import MatchEngine.Client.MessageType;
import org.junit.Before;
import org.junit.Test;

public class MessageBuilderTests {

    // Subject
    private MessageBuilder messageBuilder;

    @Before
    public void setUp(){
        messageBuilder = new MessageBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyOrderIdShouldBeNonZeroNumber(){
        messageBuilder.build(MessageType.ADDORDERREQUEST, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyOrderIdShouldBePositiveNumber(){
        messageBuilder.build(MessageType.ADDORDERREQUEST, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyMessageTypeShouldNotBeNull(){
        messageBuilder.build(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifySideShouldNotBeNullForAddRequest(){
        messageBuilder.setSide(null).build(MessageType.ADDORDERREQUEST, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyPriceShouldBeNonZeroDecimalForAddRequest(){
        messageBuilder.setPrice(0.0).build(MessageType.ADDORDERREQUEST, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyPriceShouldBePositiveDecimalForAddRequest(){
        messageBuilder.setPrice(-1.0).build(MessageType.ADDORDERREQUEST, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyQuantityShouldBeNonZeroDecimalForAddRequest(){
        messageBuilder.setQuantity(0).build(MessageType.ADDORDERREQUEST, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyQuantityShouldBePositiveDecimalForAddRequest(){
        messageBuilder.setQuantity(-1).build(MessageType.ADDORDERREQUEST, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyPriceShouldBeNonZeroDecimalForTradeEvent(){
        messageBuilder.setPrice(0.0).build(MessageType.TRADEEVENT, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyPriceShouldBePositiveDecimalForTradeEvent(){
        messageBuilder.setPrice(-1.0).build(MessageType.TRADEEVENT, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyQuantityShouldBeNonZeroDecimalForTradeEvent(){
        messageBuilder.setQuantity(0).build(MessageType.TRADEEVENT, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyQuantityShouldBePositiveDecimalForTradeEvent(){
        messageBuilder.setQuantity(-1).build(MessageType.TRADEEVENT, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyQuantityShouldBeNonZeroDecimalForPartialFillEvent(){
        messageBuilder.setQuantity(0).build(MessageType.ORDERPARTIALLYFILLED, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifyQuantityShouldBePositiveDecimalForPartialFillEvent(){
        messageBuilder.setQuantity(-1).build(MessageType.ORDERPARTIALLYFILLED, 1);
    }
}
