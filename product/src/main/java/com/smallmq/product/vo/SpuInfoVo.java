package com.smallmq.product.vo;

import com.smallmq.product.entity.SpuInfoEntity;

public class SpuInfoVo extends SpuInfoEntity {
    private static final long serialVersionUID = 1L;

    private String catelogName;

    private String brandName;

    private String productStatus;

    public String getCatelogName() {
        return catelogName;
    }

    public void setCatelogName(String catelogName) {
        this.catelogName = catelogName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
