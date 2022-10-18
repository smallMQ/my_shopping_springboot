package com.smallmq.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.common.utils.PageUtils;
import com.smallmq.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:29:12
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

