package MatchEngine.Client;

public enum Side {
    BUY(0), SELL(1);

    private final int value;

    Side(int value){
        this.value = value;
    }

    public int toInteger(){
        return value;
    }

    public static Side toSide(int s){
        switch (s){
            case 0:
                return BUY;
            case 1:
                return SELL;
        }
        throw new IllegalArgumentException("Integer " + s + " does not correspond to any valid side.");
    }
}
