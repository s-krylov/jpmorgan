package jpmorgan.core.db.dao;


import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;

import java.util.List;


public interface Dao {

    void saveMessage2(ProductType productType, SaleOccurrences occurrences);

    void saveMessage3(ProductType productType, SaleOccurrences occurrences, SaleAdjustment adjustment);

    List<SaleOccurrences> getSalesReport();

    List<SaleAdjustment> getSaleAdjustmentsReport();
}
