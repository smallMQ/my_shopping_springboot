package com.smallmq.product.dao;

import com.smallmq.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

}
