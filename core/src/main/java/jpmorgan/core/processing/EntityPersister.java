package jpmorgan.core.processing;


import jpmorgan.core.Converter;
import jpmorgan.core.db.dao.Dao;
import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;
import jpmorgan.core.db.dao.DaoFactory;
import jpmorgan.entities.Message;


public class EntityPersister  {

    private final Dao dao;

    EntityPersister(Dao dao) {
        this.dao = dao;
    }

    public EntityPersister() {
        this (DaoFactory.getInstance().create());
    }

    public void persist(Message message) {
        if (message.getMessageType1() != null) {
            ProductType productType = Converter.createProduct(message.getMessageType1().getDetails());
            SaleOccurrences saleOccurrences = Converter.createSaleOccurrences(message.getMessageType1().getDetails());
            dao.saveMessage2(productType, saleOccurrences);
        } else if (message.getMessageType2() != null) {
            ProductType productType = Converter.createProduct(message.getMessageType2().getDetails());
            SaleOccurrences saleOccurrences = Converter.createSaleOccurrences(message.getMessageType2());
            dao.saveMessage2(productType, saleOccurrences);
        } else if (message.getMessageType3() != null) {
            ProductType productType = Converter.createProduct(message.getMessageType3().getDetails());
            SaleOccurrences saleOccurrences = Converter.createSaleOccurrences(message.getMessageType3().getDetails());
            SaleAdjustment saleAdjustment = Converter.createSaleAdjustment(message.getMessageType3());
            dao.saveMessage3(productType, saleOccurrences, saleAdjustment);
        } else {
            throw new IllegalArgumentException("One of children should be filled with value");
        }
    }
}
