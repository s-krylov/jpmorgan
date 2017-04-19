package jpmorgan.core.processing;

import jpmorgan.core.Utils;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;



public class FlowManagerTest {
    @Test
    public void execute() throws Exception {
        StringWriter stringWriter = new StringWriter();
        FlowManager flowManager = new FlowManager(3, 1, new PrintWriter(stringWriter));
        flowManager.execute(Utils.readFileAsString("/xml/messageType1.xml"));
        flowManager.execute(Utils.readFileAsString("/xml/messageType2.xml"));
        flowManager.execute(Utils.readFileAsString("/xml/messageType3.xml"));
        flowManager.execute(Utils.readFileAsString("/xml/messageType1.xml"));
        assertEquals(Utils.readFileAsString("/reports/final.txt"), stringWriter.toString());
        System.out.println(stringWriter.toString());
    }

}