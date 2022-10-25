package com.smallmq.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * Auto-generated: 2022-10-25 10:33:27
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
@Data
public class Skus {

    private List<Attr> attr;
    
    private String skuName;
    private BigDecimal price;
    
    private String skuTitle;
    
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    
    private int fullCount;
    private BigDecimal discount;
    
    private int countStatus;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;
    
    private int priceStatus;
    
    private List<MemberPrice> memberPrice;


}