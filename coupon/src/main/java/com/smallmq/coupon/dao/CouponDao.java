package com.smallmq.coupon.dao;

import com.smallmq.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:13:17
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {

}
