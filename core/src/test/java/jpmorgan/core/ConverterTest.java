package jpmorgan.core;

import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;
import jpmorgan.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;



public class ConverterTest {

    private Sale sale;

    @Before
    public void setUp() {
        sale = TestHelper.createSale("watermelon", new BigDecimal(12));
    }

    @Test
    public void createProduct() throws Exception {
        ProductType productType = Converter.createProduct(sale);
        assertNotNull(productType);
        assertEquals(sale.getProductType(), productType.getProduct());
    }

    @Test
    public void createSaleOccurrences() throws Exception {
        SaleOccurrences saleOccurrences = Converter.createSaleOccurrences(sale);
        assertNotNull(saleOccurrences);
        assertEquals(Integer.valueOf(1), saleOccurrences.getOccurrences());
        assertTrue(saleOccurrences.getOriginalPrice().compareTo(sale.getValue()) == 0);
        assertTrue(saleOccurrences.getPrice().compareTo(sale.getValue()) == 0);
        assertNull(saleOccurrences.getProductType());
    }

    @Test
    public void createSaleOccurrences1() throws Exception {
        MessageType2 messageType2 = TestHelper.createMessageType2(sale, new BigInteger("20"));
        SaleOccurrences saleOccurrences = Converter.createSaleOccurrences(messageType2);
        assertNotNull(saleOccurrences);
        assertTrue(saleOccurrences.getOriginalPrice().compareTo(sale.getValue()) == 0);
        assertTrue(saleOccurrences.getPrice().compareTo(sale.getValue()) == 0);
        assertEquals(Integer.valueOf(messageType2.getOccurrences().intValue()), saleOccurrences.getOccurrences());
        assertNull(saleOccurrences.getProductType());
    }

    @Test
    public void createSaleAdjustment() throws Exception {
        MessageType3 messageType3 = TestHelper.createMessageType3(sale, new BigDecimal(100), OperationEnum.ADD);
        SaleAdjustment saleAdjustment = Converter.createSaleAdjustment(messageType3);
        assertNotNull(saleAdjustment);
        assertEquals(messageType3.getOperation().getOperation(), saleAdjustment.getAction());
        assertTrue(saleAdjustment.getOperand().compareTo(messageType3.getOperation().getAmount()) == 0);
        assertNull(saleAdjustment.getProductType());
        assertNull(saleAdjustment.getProductTypeId());
    }

}