package Client;

import MatchEngine.Client.Side;
import org.junit.Assert;
import org.junit.Test;

public class SideTests {

    @Test
    public void testToInteger(){
        Assert.assertEquals(Side.BUY.toInteger(), 0);
        Assert.assertEquals(Side.SELL.toInteger(), 1);
    }

    @Test
    public void testToSide(){
        Assert.assertEquals(Side.toSide(0), Side.BUY);
        Assert.assertEquals(Side.toSide(1), Side.SELL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSide(){
        Side.toSide(2);
    }
}
