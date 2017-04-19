package jpmorgan.core;


import jpmorgan.entities.AdjustmentOperation;
import jpmorgan.entities.Message;
import jpmorgan.entities.MessageType1;
import jpmorgan.entities.MessageType2;
import jpmorgan.entities.MessageType3;
import jpmorgan.entities.OperationEnum;
import jpmorgan.entities.Sale;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestHelper {

    public static Sale createSale(String product, BigDecimal value) {
        Sale sale = new Sale();
        sale.setProductType(product);
        sale.setValue(value);
        return sale;
    }

    public static MessageType1 createMessageType1(Sale sale) {
        MessageType1 messageType1 = new MessageType1();
        messageType1.setDetails(sale);
        return messageType1;
    }


    public static MessageType2 createMessageType2(Sale sale, BigInteger occurrences) {
        MessageType2 messageType2 = new MessageType2();
        messageType2.setDetails(sale);
        messageType2.setOccurrences(occurrences);
        return messageType2;
    }

    public static MessageType3 createMessageType3(Sale sale, BigDecimal amount, OperationEnum operationEnum) {
        AdjustmentOperation operation = new AdjustmentOperation();
        operation.setAmount(amount);
        operation.setOperation(operationEnum);
        MessageType3 messageType3 = new MessageType3();
        messageType3.setDetails(sale);
        messageType3.setOperation(operation);
        return messageType3;
    }

    public static Message createMessage(MessageType1 messageType1) {
        Message message = new Message();
        message.setMessageType1(messageType1);
        return message;
    }

    public static Message createMessage(MessageType2 messageType2) {
        Message message = new Message();
        message.setMessageType2(messageType2);
        return message;
    }

    public static Message createMessage(MessageType3 messageType3) {
        Message message = new Message();
        message.setMessageType3(messageType3);
        return message;
    }
}
