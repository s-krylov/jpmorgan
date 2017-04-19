package jpmorgan.core.processing.report;


import jpmorgan.core.db.dao.Dao;
import jpmorgan.core.db.dao.DaoFactory;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.processing.Report;

import java.io.PrintWriter;
import java.util.List;

public class AdjustmentReport implements Report {

    private final PrintWriter writer;
    private final Dao dao;

    AdjustmentReport(PrintWriter writer, Dao dao) {
        this.writer = writer;
        this.dao = dao;
    }

    public AdjustmentReport(PrintWriter writer) {
        this(writer, DaoFactory.getInstance().create());
    }

    @Override
    public void createReport() {
        writer.println("============AdjustmentReport============");
        List<SaleAdjustment> data = dao.getSaleAdjustmentsReport();
        data.forEach(saleAdjustment -> {
            writer.printf("product = %s, operation = %s, operand = %s%n", saleAdjustment.getProductType().getProduct(),
                    saleAdjustment.getAction(), saleAdjustment.getOperand());
        });
        writer.println("========================================");
        writer.println("========================================");
        writer.println("========================================");
    }
}
