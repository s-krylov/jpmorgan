package jpmorgan.core.processing;


import jpmorgan.core.processing.report.AdjustmentReport;
import jpmorgan.core.processing.report.SalesPerProductReport;
import jpmorgan.entities.Message;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class FlowManager implements Closeable {

    private final int maximumMessages;
    private final int reportPerMessages;
    private final PrintWriter writer;
    private int messagesProcessed;

    public FlowManager(int maximumMessages, int reportPerMessages) {
        this(maximumMessages, reportPerMessages, new PrintWriter(System.out));
    }

    public FlowManager(int maximumMessages, int reportPerMessages, PrintWriter writer) {
        this.maximumMessages = maximumMessages;
        this.reportPerMessages = reportPerMessages;
        this.writer = writer;
        this.messagesProcessed = 0;
    }

    public void execute(String xml) {
        if (messagesProcessed >= maximumMessages) {
            writer.println("Maximum processed messages achieved");
            return;
        }

        MessageParser messageParser = new MessageParser();
        Message message = messageParser.parse(xml);

        EntityPersister entityPersister = new EntityPersister();
        entityPersister.persist(message);

        List<Report> reports = new ArrayList<>();
        int processed = messagesProcessed + 1;

        if (processed % reportPerMessages == 0) {
            reports.add(new SalesPerProductReport(writer));
        }

        if (processed == maximumMessages) {
            reports.add(new AdjustmentReport(writer));
        }

        reports.forEach(Report::createReport);

        messagesProcessed = processed;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
