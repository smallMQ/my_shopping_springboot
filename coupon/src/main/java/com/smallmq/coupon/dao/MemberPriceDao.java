package com.smallmq.coupon.dao;

import com.smallmq.coupon.entity.MemberPriceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:13:17
 */
@Mapper
public interface MemberPriceDao extends BaseMapper<MemberPriceEntity> {

}
