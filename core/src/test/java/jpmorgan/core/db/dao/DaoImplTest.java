package jpmorgan.core.db.dao;

import jpmorgan.core.db.DataSourceFactory;
import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;
import jpmorgan.entities.OperationEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


public class DaoImplTest {

    private Dao dao;
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        dao = new DaoImpl();
        dataSource = DataSourceFactory.getInstance().createDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    public void cleanUp() {
        jdbcTemplate.execute("TRUNCATE SCHEMA public AND COMMIT");
    }

    private ProductType createProductType(String product) {
        ProductType productType = new ProductType();
        productType.setProduct(product);
        return productType;
    }

    private SaleOccurrences createSaleOccurrences(BigDecimal price, int occurrences) {
        SaleOccurrences saleOccurrences = new SaleOccurrences();
        saleOccurrences.setPrice(price);
        saleOccurrences.setOriginalPrice(price);
        saleOccurrences.setOccurrences(occurrences);
        return saleOccurrences;
    }

    private SaleAdjustment createSaleAdjustment(BigDecimal operand, OperationEnum operationEnum) {
        SaleAdjustment saleAdjustment = new SaleAdjustment();
        saleAdjustment.setOperand(operand);
        saleAdjustment.setAction(operationEnum);
        return saleAdjustment;
    }

    private Long getProductIdBy(String name) {
        return jdbcTemplate.queryForObject("SELECT ID FROM PRODUCT_TYPE WHERE PRODUCT=?", new Object[] {name}, Long.class);
    }

    private Map<String, Object> getSaleBy(Long productId) {
        return jdbcTemplate.queryForMap("SELECT ORIGINAL_PRICE, PRICE, OCCURRENCES FROM SALE WHERE PRODUCT_TYPE_ID = ?",
                productId);
    }

    private Map<String, Object> getAdjustmentBy(Long productId) {
        return jdbcTemplate.queryForMap("SELECT ACTION, OPERAND FROM ADJUSTMENTS WHERE PRODUCT_TYPE_ID = ?",
                productId);
    }

    @Test
    public void saveMessage2() throws Exception {
        dao.saveMessage2(createProductType("apple"), createSaleOccurrences(new BigDecimal(31), 3));

        Long id = getProductIdBy("apple");
        assertNotNull(id);

        Map<String, Object> sale = getSaleBy(id);
        assertNotNull(sale);
        BigDecimal price = (BigDecimal) sale.get("PRICE");
        assertNotNull(price);
        assertTrue(price.compareTo(new BigDecimal(31)) == 0);
        BigDecimal originalPrice = (BigDecimal) sale.get("ORIGINAL_PRICE");
        assertNotNull(originalPrice);
        assertTrue(originalPrice.compareTo(new BigDecimal(31)) == 0);
        Integer occurrences = (Integer) sale.get("OCCURRENCES");
        assertNotNull(occurrences);
        assertEquals(new Integer(3), occurrences);
    }

    @Test
    public void saveMessage3() throws Exception {
        dao.saveMessage3(createProductType("orange"), createSaleOccurrences(new BigDecimal(11), 21),
                createSaleAdjustment(new BigDecimal(2), OperationEnum.SUBTRACT));

        Long id = getProductIdBy("orange");
        assertNotNull(id);

        Map<String, Object> sale = getSaleBy(id);
        assertNotNull(sale);
        BigDecimal price = (BigDecimal) sale.get("PRICE");
        assertNotNull(price);
        assertTrue(price.compareTo(new BigDecimal(9)) == 0);
        BigDecimal originalPrice = (BigDecimal) sale.get("ORIGINAL_PRICE");
        assertNotNull(originalPrice);
        assertTrue(originalPrice.compareTo(new BigDecimal(11)) == 0);
        Integer occurrences = (Integer) sale.get("OCCURRENCES");
        assertNotNull(occurrences);
        assertEquals(new Integer(21), occurrences);

        Map<String, Object> adjustment = getAdjustmentBy(id);
        assertNotNull(adjustment);
        String operation = (String) adjustment.get("ACTION");
        assertEquals("SUBTRACT", operation);
        BigDecimal operand = (BigDecimal) adjustment.get("OPERAND");
        assertNotNull(operand);
        assertTrue(operand.compareTo(new BigDecimal(2)) == 0);
    }

    @Test
    public void getSalesReport() throws Exception {
        dao.saveMessage2(createProductType("apple"), createSaleOccurrences(new BigDecimal(31), 3));
        dao.saveMessage3(createProductType("orange"), createSaleOccurrences(new BigDecimal(11), 21),
                createSaleAdjustment(new BigDecimal(2), OperationEnum.SUBTRACT));
        dao.saveMessage3(createProductType("apple"), createSaleOccurrences(new BigDecimal(3), 10),
                createSaleAdjustment(new BigDecimal(3), OperationEnum.MULTIPLY));
        List<SaleOccurrences> report = dao.getSalesReport();
        assertNotNull(report);
        assertEquals(2, report.size());
        Optional<SaleOccurrences> apple = report.stream().filter(s -> "apple".equals(s.getProductType().getProduct())).findFirst();
        assertTrue(apple.isPresent() && apple.get().getPrice().compareTo(new BigDecimal(369)) == 0);
        Optional<SaleOccurrences> orange = report.stream().filter(s -> "orange".equals(s.getProductType().getProduct())).findFirst();
        assertTrue(orange.isPresent() && orange.get().getPrice().compareTo(new BigDecimal(189)) == 0);
    }

    @Test
    public void getSaleAdjustmentsReport() throws Exception {
        dao.saveMessage3(createProductType("apple"), createSaleOccurrences(new BigDecimal(3), 10),
                createSaleAdjustment(new BigDecimal(3), OperationEnum.MULTIPLY));
        dao.saveMessage3(createProductType("orange"), createSaleOccurrences(new BigDecimal(11), 21),
                createSaleAdjustment(new BigDecimal(2), OperationEnum.SUBTRACT));
        dao.saveMessage3(createProductType("apple"), createSaleOccurrences(new BigDecimal(5), 7),
                createSaleAdjustment(new BigDecimal(1), OperationEnum.ADD));
        List<SaleAdjustment> report = dao.getSaleAdjustmentsReport();
        assertEquals(3, report.size());
        SaleAdjustment adjustment = report.get(0);
        assertEquals("apple", adjustment.getProductType().getProduct());
        assertEquals(OperationEnum.MULTIPLY, adjustment.getAction());
        assertTrue(adjustment.getOperand().compareTo(new BigDecimal(3)) == 0);

        adjustment = report.get(1);
        assertEquals("orange", adjustment.getProductType().getProduct());
        assertEquals(OperationEnum.SUBTRACT, adjustment.getAction());
        assertTrue(adjustment.getOperand().compareTo(new BigDecimal(2)) == 0);

        adjustment = report.get(2);
        assertEquals("apple", adjustment.getProductType().getProduct());
        assertEquals(OperationEnum.ADD, adjustment.getAction());
        assertTrue(adjustment.getOperand().compareTo(new BigDecimal(1)) == 0);
    }

}