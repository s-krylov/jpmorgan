package jpmorgan.core;


import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;
import jpmorgan.entities.MessageType2;
import jpmorgan.entities.MessageType3;
import jpmorgan.entities.Sale;


public class Converter {

    private Converter() {}

    public static ProductType createProduct(Sale sale) {
        ProductType productType = new ProductType();
        productType.setProduct(sale.getProductType());
        return productType;
    }

    public static SaleOccurrences createSaleOccurrences(Sale sale) {
        SaleOccurrences saleOccurrences = new SaleOccurrences();
        saleOccurrences.setPrice(sale.getValue());
        saleOccurrences.setOriginalPrice(sale.getValue());
        saleOccurrences.setOccurrences(1);
        return saleOccurrences;
    }

    public static SaleOccurrences createSaleOccurrences(MessageType2 messageType2) {
        SaleOccurrences saleOccurrences = new SaleOccurrences();
        saleOccurrences.setPrice(messageType2.getDetails().getValue());
        saleOccurrences.setOriginalPrice(messageType2.getDetails().getValue());
        saleOccurrences.setOccurrences(messageType2.getOccurrences().intValue());
        return saleOccurrences;
    }

    public static SaleAdjustment createSaleAdjustment(MessageType3 messageType3) {
        SaleAdjustment saleAdjustment = new SaleAdjustment();
        saleAdjustment.setAction(messageType3.getOperation().getOperation());
        saleAdjustment.setOperand(messageType3.getOperation().getAmount());
        return saleAdjustment;
    }
}
