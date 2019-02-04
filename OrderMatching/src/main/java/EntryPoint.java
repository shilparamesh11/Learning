import MatchEngine.Server.API.IMessage;
import MatchEngine.Client.MatchEngineClient;
import MatchEngine.Server.API.IMessageBuilder;
import MatchEngine.Server.MessageBuilder;
import MatchEngine.Client.MessageType;
import MatchEngine.Client.Side;
import MatchEngine.Server.ServerFactory;

import java.util.Scanner;

class EntryPoint {
    public static void main(String[] args) {
        System.out.println("Setting up the client");
        MatchEngineClient engine = new MatchEngineClient();
        IMessageBuilder messageBuilder = ServerFactory.getMessageBuilder();
        System.out.println("Client setup completed");

        System.out.println("================================================================================");
        System.out.println("Information below will help you get started entering the trades");
        System.out.println("================================================================================");
        System.out.println("Order side : Buy = 0 ; Sell = 0");
        System.out.println("Input messages other than 0 = AddOrderRequest or 1 = CancelOrderRequest, will be ignored");
        System.out.println("Add order request should be in format : 0,1,0,1,15.0987");
        System.out.println("First element in the comma separated string represents messageType as described above");
        System.out.println("Second element int string represents orderId which is positive integer");
        System.out.println("Third element in string side of order as described above");
        System.out.println("Fourth element in the string represents quantity must be a positive integer");
        System.out.println("Fifth element in the string represents price and should be a positive decimal number");
        System.out.println("Cancel order request should be in format : 1,2");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("First element in the comma separated string represents messageType as described above");
        System.out.println("Second element int string represents orderId which is positive integer");
        System.out.println("================================================================================");

        try {
            Scanner in = new Scanner(System.in);

            while (true) {
                System.out.println("Please enter order : ");
                try {
                    String orderInfo = in.nextLine();
                    IMessage message = toMessage(messageBuilder, orderInfo);

                    if (message.getMessageType() == MessageType.ADDORDERREQUEST)
                        engine.matchOrder(message);
                    else if (message.getMessageType() == MessageType.CANCELORDERREQUEST)
                        engine.removeOrder(message);
                    else
                        System.out.println("Not a valid input message type. Ignoring the input..");

                } catch (IllegalArgumentException arg) {
                    System.out.println(arg.getMessage());
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // convert string to message object
    private static IMessage toMessage(IMessageBuilder messageBuilder, String input){
        try{
            String[] values = input.split(",");
            if (values.length != 2 && values.length != 5) throw new IllegalArgumentException("Order information should have either 2 or 5 elements");
            int mt = Integer.valueOf(values[0].trim());
            long orderId = Long.valueOf(values[1].trim());
            if(values.length == 2) return new MessageBuilder().build(MessageType.toMessageType(mt), orderId);
            int s = Integer.valueOf(values[2].trim());
            int quantity = Integer.valueOf(values[3].trim());
            double price = Double.valueOf(values[4].trim());
            return messageBuilder.setSide(Side.toSide(s)).setQuantity(quantity).setPrice(price).build(MessageType.toMessageType(mt), orderId);
        } catch (Exception e){
            throw new IllegalArgumentException("Unable to parse input. " + e.getMessage());
        }
    }
}
