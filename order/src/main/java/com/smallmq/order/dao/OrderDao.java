package com.smallmq.order.dao;

import com.smallmq.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:27:46
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

}
