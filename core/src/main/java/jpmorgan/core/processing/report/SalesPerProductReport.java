package jpmorgan.core.processing.report;


import jpmorgan.core.db.dao.Dao;
import jpmorgan.core.db.dao.DaoFactory;
import jpmorgan.core.db.pojo.SaleOccurrences;
import jpmorgan.core.processing.Report;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;


public class SalesPerProductReport implements Report {

    private final PrintWriter writer;
    private final Dao dao;

    SalesPerProductReport(PrintWriter writer, Dao dao) {
        this.writer = writer;
        this.dao = dao;
    }

    public SalesPerProductReport(PrintWriter writer) {
        this(writer, DaoFactory.getInstance().create());
    }

    @Override
    public void createReport() {
        writer.println("=========SalesPerProductReport=========");
        List<SaleOccurrences> data = dao.getSalesReport();
        data.forEach(saleOccurrences -> {
            writer.printf("product = %s, price = %s, occurrences = %s%n", saleOccurrences.getProductType().getProduct(),
                    saleOccurrences.getPrice(), saleOccurrences.getOccurrences());
        });
        writer.println("total price = " + data.stream().map(SaleOccurrences::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        writer.println("========================================");
        writer.println("========================================");
        writer.println("========================================");
    }
}
