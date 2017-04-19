package jpmorgan.core.mock;

import jpmorgan.core.db.dao.Dao;
import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;

import java.util.Collections;
import java.util.List;


public class MockedDao implements Dao {

    private ProductType productType;
    private SaleOccurrences occurrences;
    private SaleAdjustment adjustment;
    private List<SaleOccurrences> salesReport;
    private List<SaleAdjustment> saleAdjustmentsReport;

    public MockedDao(List<SaleOccurrences> salesReport, List<SaleAdjustment> saleAdjustmentsReport) {
        this.salesReport = salesReport;
        this.saleAdjustmentsReport = saleAdjustmentsReport;
    }

    public MockedDao() {
        this(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void saveMessage2(ProductType productType, SaleOccurrences occurrences) {
        this.productType = productType;
        this.occurrences = occurrences;
    }

    @Override
    public void saveMessage3(ProductType productType, SaleOccurrences occurrences, SaleAdjustment adjustment) {
        this.productType = productType;
        this.occurrences = occurrences;
        this.adjustment = adjustment;
    }

    @Override
    public List<SaleOccurrences> getSalesReport() {
        return salesReport;
    }

    @Override
    public List<SaleAdjustment> getSaleAdjustmentsReport() {
        return saleAdjustmentsReport;
    }

    public ProductType getProductType() {
        return productType;
    }

    public SaleOccurrences getOccurrences() {
        return occurrences;
    }

    public SaleAdjustment getAdjustment() {
        return adjustment;
    }
}
