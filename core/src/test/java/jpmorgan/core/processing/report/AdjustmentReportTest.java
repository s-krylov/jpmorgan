package jpmorgan.core.processing.report;

import jpmorgan.core.Utils;
import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.mock.MockedDao;
import jpmorgan.entities.OperationEnum;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;



public class AdjustmentReportTest {

    @Test
    public void createReport() throws Exception {
        StringWriter stringWriter = new StringWriter();
        ProductType productType1 = new ProductType();
        productType1.setProduct("apple");
        SaleAdjustment saleAdjustment1 = new SaleAdjustment();
        saleAdjustment1.setProductType(productType1);
        saleAdjustment1.setOperand(new BigDecimal(12));
        saleAdjustment1.setAction(OperationEnum.SUBTRACT);

        ProductType productType2 = new ProductType();
        productType2.setProduct("orange");
        SaleAdjustment saleAdjustment2 = new SaleAdjustment();
        saleAdjustment2.setProductType(productType2);
        saleAdjustment2.setOperand(new BigDecimal(3));
        saleAdjustment2.setAction(OperationEnum.MULTIPLY);


        MockedDao mockedDao = new MockedDao(Collections.emptyList(), Arrays.asList(saleAdjustment1, saleAdjustment2));
        AdjustmentReport adjustmentReport = new AdjustmentReport(new PrintWriter(stringWriter), mockedDao);
        adjustmentReport.createReport();
        assertEquals(Utils.readFileAsString("/reports/saleAdjustmentReport.txt"), stringWriter.toString());
    }

}