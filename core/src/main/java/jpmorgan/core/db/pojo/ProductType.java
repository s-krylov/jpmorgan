package jpmorgan.core.db.pojo;



public class ProductType {

    private Long id;
    private String product;

    public ProductType() {
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
