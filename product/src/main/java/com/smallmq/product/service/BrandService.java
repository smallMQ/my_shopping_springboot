package com.smallmq.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.common.utils.PageUtils;
import com.smallmq.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

