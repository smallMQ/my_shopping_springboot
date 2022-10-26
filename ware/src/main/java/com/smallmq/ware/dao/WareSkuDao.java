package com.smallmq.ware.dao;

import com.smallmq.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:29:12
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Long getSkuStock(@Param("skuId") Long skuId);
}
