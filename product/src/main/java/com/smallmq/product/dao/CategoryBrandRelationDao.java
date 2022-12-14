package com.smallmq.product.dao;

import com.smallmq.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Ʒ�Ʒ������
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-23 18:57:19
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateCategory(@Param("catelogId") Long catId, @Param("catelogName") String  name);
}
