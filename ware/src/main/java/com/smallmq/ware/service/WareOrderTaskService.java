package com.smallmq.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.common.utils.PageUtils;
import com.smallmq.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:29:12
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

