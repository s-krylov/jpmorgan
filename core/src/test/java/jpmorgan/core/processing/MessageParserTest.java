package jpmorgan.core.processing;

import jpmorgan.core.Utils;
import jpmorgan.entities.Message;
import jpmorgan.entities.OperationEnum;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;



public class MessageParserTest {

    @Test
    public void parse() throws Exception {
        MessageParser messageParser = new MessageParser();
        Message message = messageParser.parse(Utils.readFileAsString("/xml/messageType1.xml"));
        assertNotNull(message);
        assertNull(message.getMessageType2());
        assertNull(message.getMessageType3());
        assertNotNull(message.getMessageType1());
        assertNotNull(message.getMessageType1().getDetails());
        assertEquals("apple", message.getMessageType1().getDetails().getProductType());
        assertTrue(message.getMessageType1().getDetails().getValue().compareTo(new BigDecimal(20)) == 0);

        message = messageParser.parse(Utils.readFileAsString("/xml/messageType2.xml"));
        assertNotNull(message);
        assertNull(message.getMessageType1());
        assertNull(message.getMessageType3());
        assertNotNull(message.getMessageType2());
        assertNotNull(message.getMessageType2().getDetails());
        assertTrue(message.getMessageType2().getOccurrences().compareTo(new BigInteger("100")) == 0);
        assertEquals("banana", message.getMessageType2().getDetails().getProductType());
        assertTrue(message.getMessageType2().getDetails().getValue().compareTo(new BigDecimal(10)) == 0);


        message = messageParser.parse(Utils.readFileAsString("/xml/messageType3.xml"));
        assertNotNull(message);
        assertNull(message.getMessageType1());
        assertNull(message.getMessageType2());
        assertNotNull(message.getMessageType3());
        assertNotNull(message.getMessageType3().getDetails());
        assertNotNull(message.getMessageType3().getOperation());
        assertTrue(message.getMessageType3().getOperation().getAmount().compareTo(new BigDecimal(10)) == 0);
        assertEquals(OperationEnum.ADD, message.getMessageType3().getOperation().getOperation());
        assertEquals("banana", message.getMessageType3().getDetails().getProductType());
        assertTrue(message.getMessageType3().getDetails().getValue().compareTo(new BigDecimal(4)) == 0);
    }

    @Test(expected = RuntimeException.class)
    public void parseWithException() throws Exception {
        MessageParser messageParser = new MessageParser();
        messageParser.parse(Utils.readFileAsString("/xml/notFollowSchema.xml"));
    }

}