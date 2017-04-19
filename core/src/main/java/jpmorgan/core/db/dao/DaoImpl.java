package jpmorgan.core.db.dao;

import jpmorgan.core.db.DataSourceFactory;
import jpmorgan.core.db.pojo.ProductType;
import jpmorgan.core.db.pojo.SaleAdjustment;
import jpmorgan.core.db.pojo.SaleOccurrences;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;


public class DaoImpl implements Dao {

    // SAVING QUERIES
    private static final String SELECT_PRODUCT_ID = "SELECT ID FROM PRODUCT_TYPE WHERE PRODUCT=:product";
    private static final String INSERT_PRODUCT_TYPE = "INSERT INTO PRODUCT_TYPE (PRODUCT) VALUES (:product)";
    private static final String INSERT_SALE =
            "INSERT INTO SALE (PRODUCT_TYPE_ID, ORIGINAL_PRICE, PRICE, OCCURRENCES) VALUES " +
                    "(:productTypeId, :originalPrice, :price, :occurrences)";
    private static final String UPDATE_SALE_ADD = "UPDATE SALE SET PRICE=PRICE + :operand WHERE PRODUCT_TYPE_ID=:productTypeId";
    private static final String UPDATE_SALE_SUBTRACT = "UPDATE SALE SET PRICE=PRICE - :operand WHERE PRODUCT_TYPE_ID=:productTypeId";
    private static final String UPDATE_SALE_MULTIPLY = "UPDATE SALE SET PRICE=PRICE * :operand WHERE PRODUCT_TYPE_ID=:productTypeId";
    private static final String INSERT_ADJUSTMENTS =
            "INSERT INTO ADJUSTMENTS (PRODUCT_TYPE_ID, ACTION, OPERAND) VALUES " +
                    "(:productTypeId, :actionAsString, :operand)";
    // REPORTS QUERIES
    private static final String SELECT_SALES = "SELECT PRODUCT_TYPE_ID, SUM(OCCURRENCES) AS OCCURRENCES, " +
            "SUM(OCCURRENCES * PRICE) AS PRICE FROM SALE GROUP BY (PRODUCT_TYPE_ID) ORDER BY PRODUCT_TYPE_ID ASC";
    private static final String SELECT_ADJUSTMENTS = "SELECT ID, PRODUCT_TYPE_ID, ACTION as actionAsString, OPERAND FROM ADJUSTMENTS ORDER BY ID ASC";
    private static final String SELECT_PRODUCT_TYPE = "SELECT ID, PRODUCT FROM PRODUCT_TYPE WHERE ID=:productTypeId";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DaoImpl() {
        this.dataSource = DataSourceFactory.getInstance().createDataSource();
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private void save(ProductType productType) {
        List<Long> ids = jdbcTemplate.queryForList(SELECT_PRODUCT_ID, new BeanPropertySqlParameterSource(productType), Long.class);
        if (!ids.isEmpty()) {
            productType.setId(ids.get(0));
            return;
        }
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_PRODUCT_TYPE, new BeanPropertySqlParameterSource(productType), keyHolder);
        Number key = keyHolder.getKey();
        productType.setId(key.longValue());
    }

    private void save(SaleOccurrences occurrences) {
        jdbcTemplate.update(INSERT_SALE, new BeanPropertySqlParameterSource(occurrences));
    }

    private void save(SaleAdjustment adjustment) {
        String updateSalePriceQuery;
        switch (adjustment.getAction()) {
            case ADD: updateSalePriceQuery = UPDATE_SALE_ADD; break;
            case SUBTRACT: updateSalePriceQuery = UPDATE_SALE_SUBTRACT; break;
            case MULTIPLY: updateSalePriceQuery = UPDATE_SALE_MULTIPLY; break;
            default: throw new IllegalArgumentException("Unsupported adjustment type");
        }
        jdbcTemplate.update(updateSalePriceQuery, new BeanPropertySqlParameterSource(adjustment));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_ADJUSTMENTS, new BeanPropertySqlParameterSource(adjustment), keyHolder);
        Number key = keyHolder.getKey();
        adjustment.setId(key.longValue());
    }

    private void doInTransaction(Consumer<TransactionStatus> action) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            action.accept(status);
            return null;
        });
    }

    @Override
    public void saveMessage2(ProductType productType, SaleOccurrences occurrences) {
        doInTransaction(transactionStatus -> {
            save(productType);
            occurrences.setProductTypeId(productType.getId());
            save(occurrences);
        });
    }

    @Override
    public void saveMessage3(ProductType productType, SaleOccurrences occurrences, SaleAdjustment adjustment) {
        doInTransaction(transactionStatus -> {
            save(productType);
            occurrences.setProductTypeId(productType.getId());
            adjustment.setProductTypeId(productType.getId());
            save(occurrences);
            save(adjustment);
        });
    }

    @Override
    public List<SaleOccurrences> getSalesReport() {
        List<SaleOccurrences> saleOccurrences = jdbcTemplate.query(SELECT_SALES, new BeanPropertyRowMapper<>(SaleOccurrences.class));
        Map<Long, ProductType> cache = new WeakHashMap<>();
        saleOccurrences.forEach(saleOccurrence -> {
            Long key = saleOccurrence.getProductTypeId();
            // caching functionality not helping here - it was decided to keep it to follow general approach
            ProductType productType = getProductType(cache, key, saleOccurrence);
            saleOccurrence.setProductType(productType);
        });
        return saleOccurrences;
    }

    @Override
    public List<SaleAdjustment> getSaleAdjustmentsReport() {
        List<SaleAdjustment> adjustments = jdbcTemplate.query(SELECT_ADJUSTMENTS, new BeanPropertyRowMapper<>(SaleAdjustment.class));
        Map<Long, ProductType> cache = new WeakHashMap<>();
        adjustments.forEach(saleAdjustment -> {
            Long key = saleAdjustment.getProductTypeId();
            ProductType productType = getProductType(cache, key, saleAdjustment);
            saleAdjustment.setProductType(productType);
        });
        return adjustments;
    }

    private ProductType getProductType(Map<Long, ProductType> cache, Long key, Object namedParameterSource) {
        return cache.compute(key, (id, oldProductType) -> {
            if (oldProductType == null) {
                oldProductType = jdbcTemplate.queryForObject(SELECT_PRODUCT_TYPE, new BeanPropertySqlParameterSource(namedParameterSource),
                        new BeanPropertyRowMapper<>(ProductType.class));
            }
            return oldProductType;
        });
    }

}
