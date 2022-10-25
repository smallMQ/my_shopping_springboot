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
public class SpuSaveVo {


    private String spuName;
    
    private String spuDescription;
    
    private Long catalogId;
    
    private Long brandId;
    private BigDecimal weight;
    
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    
    private List<Baseattrs> baseAttrs;
    private List<Skus> skus;


}