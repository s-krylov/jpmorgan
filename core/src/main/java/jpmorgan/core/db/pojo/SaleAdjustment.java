package jpmorgan.core.db.pojo;


import jpmorgan.entities.OperationEnum;

import java.math.BigDecimal;


public class SaleAdjustment {

    private Long id;
    private Long productTypeId;
    private ProductType productType;
    private OperationEnum action;
    private BigDecimal operand;

    public SaleAdjustment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public OperationEnum getAction() {
        return action;
    }

    public String getActionAsString() {
        return action.name();
    }

    public void setActionAsString(String action) {
        this.action = Enum.valueOf(OperationEnum.class, action.toUpperCase());
    }

    public void setAction(OperationEnum action) {
        this.action = action;
    }

    public BigDecimal getOperand() {
        return operand;
    }

    public void setOperand(BigDecimal operand) {
        this.operand = operand;
    }
}
