package com.smallmq.order.dao;

import com.smallmq.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:27:46
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {

}
