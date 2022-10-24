package com.smallmq.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.product.entity.AttrEntity;
import com.smallmq.product.vo.AttrGroupVo;
import com.smallmq.utils.PageUtils;
import com.smallmq.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getAttrRelation(Long attrgroupId);

    PageUtils getAttrNoRelation(Map<String, Object> params, Long attrgroupId);

    List<AttrGroupVo> getAttrGroupWithAttrs(Long catelogId);
}

