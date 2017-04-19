package jpmorgan.core;

import org.junit.Test;

import static org.junit.Assert.*;



public class UtilsTest {

    @Test
    public void readFileAsString() throws Exception {
        assertEquals("1\r\n2\r\n3", Utils.readFileAsString("/test.txt"));
    }

    @Test(expected = NullPointerException.class)
    public void readFileAsStringNPE() throws Exception {
        assertEquals("1\n2\n3", Utils.readFileAsString("test.txt"));
    }

    @Test(expected = RuntimeException.class)
    public void suppressThrowsRuntimeException() throws Exception {
        Utils.suppressThrows(() -> {
            throw new Exception("passed");
        });
    }

    @Test
    public void suppressThrows() throws Exception {
        assertEquals("success", Utils.suppressThrows(() -> "success"));
    }

    @Test
    public void convertToRuntimeException() throws Exception {
        Exception e = new Exception("test");
        Exception converted = Utils.convertToRuntimeException(e);
        assertTrue(converted instanceof RuntimeException);
        assertNotNull(converted.getCause());
        assertFalse(converted.getCause() instanceof RuntimeException);

        converted = Utils.convertToRuntimeException(converted);
        assertTrue(converted instanceof RuntimeException);
        assertNotNull(converted.getCause());
        assertFalse(converted.getCause() instanceof RuntimeException);
    }

}