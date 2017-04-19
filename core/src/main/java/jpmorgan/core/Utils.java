package jpmorgan.core;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Utils {

    private static <A> A readUsingCollector(InputStream is, Collector<? super String, ?, A> collector) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(collector);
        } catch (IOException ioe) {
            throw convertToRuntimeException(ioe);
        }
    }

    public static String readFileAsString(String path) {
        try (InputStream is = Utils.class.getResourceAsStream(path)) {
            return readUsingCollector(is, Collectors.joining("\r\n"));
        } catch (IOException ioe) {
            throw convertToRuntimeException(ioe);
        }
    }

    public static <E> E suppressThrows(SupplierThrowsException<E> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw convertToRuntimeException(e);
        }
    }

    public static RuntimeException convertToRuntimeException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }

    @FunctionalInterface
    public interface SupplierThrowsException<E> {

        E get() throws Exception;
    }
}
