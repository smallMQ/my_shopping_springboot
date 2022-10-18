package com.smallmq.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.common.utils.PageUtils;
import com.smallmq.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

