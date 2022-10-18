package com.smallmq.order.dao;

import com.smallmq.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:27:46
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {

}
