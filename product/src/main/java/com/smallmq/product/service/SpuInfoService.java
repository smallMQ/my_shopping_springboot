package com.smallmq.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.common.utils.PageUtils;
import com.smallmq.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

