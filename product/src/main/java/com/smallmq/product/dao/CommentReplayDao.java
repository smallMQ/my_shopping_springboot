package com.smallmq.product.dao;

import com.smallmq.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {

}
