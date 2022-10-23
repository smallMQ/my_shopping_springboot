package com.smallmq.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smallmq.product.entity.CategoryBrandRelationEntity;
import com.smallmq.utils.PageUtils;

import java.util.Map;

/**
 * Ʒ�Ʒ������
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-23 18:57:19
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String name);
}

