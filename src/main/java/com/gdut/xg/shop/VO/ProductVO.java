package com.gdut.xg.shop.VO;


import com.gdut.xg.shop.entity.Category;
import com.gdut.xg.shop.entity.Product;
import lombok.Data;

@Data
public class ProductVO {
    private String id;
    private String name;
    private Category category;
    private Float price;
    private Integer stock;
    private String imgurl;
    private Integer isNew;
    private Integer discount;
    private String description;
    private String relative;


    public ProductVO(Product product, Category category) {
        super();
        this.id = product.getId();
        this.name = product.getName();
        this.category = category;
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.isNew = product.getIsNew();
        this.imgurl = product.getProductImg() == null ? "" : product.getProductImg();
        this.description = product.getDescription() == null ? "" : product.getDescription();
        this.discount = product.getDiscount();
    }

    public ProductVO() {
        super();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductVO) {
            ProductVO p = (ProductVO) obj;
            return this.id.equals(p.getId());
        } else {
            return false;
        }

        // TODO Auto-generated method stub

    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 31 * id.length() + id.hashCode();
    }

}
