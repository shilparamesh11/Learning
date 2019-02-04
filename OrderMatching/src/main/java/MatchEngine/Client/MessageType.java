package MatchEngine.Client;

public enum MessageType {
    ADDORDERREQUEST(0),
    CANCELORDERREQUEST(1),
    TRADEEVENT(2),
    ORDERFULLYFILLED(3),
    ORDERPARTIALLYFILLED(4);

    private final int value;

    MessageType(int value){
        this.value = value;
    }

    public int toInteger(){
        return value;
    }

    public static MessageType toMessageType(int m){
        switch (m){
            case 0:
                return ADDORDERREQUEST;
            case 1:
                return CANCELORDERREQUEST;
            case 2:
                return TRADEEVENT;
            case 3:
                return ORDERFULLYFILLED;
            case 4:
                return ORDERPARTIALLYFILLED;
        }
        throw new IllegalArgumentException("Integer " + m + " does not correspond to any valid message type.");
    }
}
