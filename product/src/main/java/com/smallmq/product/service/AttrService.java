package com.smallmq.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.product.vo.AttrResVo;
import com.smallmq.product.vo.AttrVo;
import com.smallmq.utils.PageUtils;
import com.smallmq.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrResVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

