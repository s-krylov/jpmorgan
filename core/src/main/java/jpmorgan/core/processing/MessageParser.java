package jpmorgan.core.processing;



import jpmorgan.core.Utils;
import jpmorgan.entities.Message;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;


public class MessageParser {

    public Message parse (String xml) {
        return Utils.suppressThrows(() -> {
            JAXBContext context = JAXBContext.newInstance(Message.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI ).newSchema(MessageParser.class.getResource("/entities.xsd")));
            return (Message) unmarshaller.unmarshal(new StringReader(xml));
        });
    }
}
