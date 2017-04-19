package jpmorgan.core.processing.report;

import jpmorgan.core.Utils;
import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleOccurrences;
import jpmorgan.core.mock.MockedDao;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;



public class SalesPerProductReportTest {

    @Test
    public void createReport() throws Exception {
        StringWriter stringWriter = new StringWriter();

        ProductType productType1 = new ProductType();
        productType1.setProduct("cucumber");
        SaleOccurrences saleOccurrences1 = new SaleOccurrences();
        saleOccurrences1.setProductType(productType1);
        saleOccurrences1.setPrice(new BigDecimal(4));
        saleOccurrences1.setOccurrences(33);

        ProductType productType2 = new ProductType();
        productType2.setProduct("coffee beans");
        SaleOccurrences saleOccurrences2 = new SaleOccurrences();
        saleOccurrences2.setProductType(productType2);
        saleOccurrences2.setPrice(new BigDecimal(50));
        saleOccurrences2.setOccurrences(100);

        ProductType productType3 = new ProductType();
        productType3.setProduct("bread");
        SaleOccurrences saleOccurrences3 = new SaleOccurrences();
        saleOccurrences3.setProductType(productType3);
        saleOccurrences3.setPrice(new BigDecimal(1));
        saleOccurrences3.setOccurrences(1);


        MockedDao mockedDao = new MockedDao(Arrays.asList(saleOccurrences1, saleOccurrences2, saleOccurrences3),
                Collections.emptyList());

        SalesPerProductReport salesPerProductReport = new SalesPerProductReport(new PrintWriter(stringWriter), mockedDao);
        salesPerProductReport.createReport();
        assertEquals(Utils.readFileAsString("/reports/salesPerProductReport.txt"), stringWriter.toString());
    }

}