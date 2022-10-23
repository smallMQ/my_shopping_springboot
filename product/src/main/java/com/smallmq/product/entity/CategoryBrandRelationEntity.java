package com.smallmq.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

    import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Ʒ�Ʒ������
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-23 18:57:19
 */
@Data
@TableName("pms_category_brand_relation")
public class CategoryBrandRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

            /**
         * 
         */
                @TableId
            private Long id;
            /**
         * Ʒ��id
         */
            private Long brandId;
            /**
         * ����id
         */
            private Long catelogId;
            /**
         * 
         */
            private String brandName;
            /**
         * 
         */
            private String catelogName;
    
}
