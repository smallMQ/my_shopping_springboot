package com.smallmq.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.common.utils.PageUtils;
import com.smallmq.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:13:17
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

