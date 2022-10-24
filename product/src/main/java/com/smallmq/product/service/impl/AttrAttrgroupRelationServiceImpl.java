package com.smallmq.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.product.dao.AttrAttrgroupRelationDao;
import com.smallmq.product.entity.AttrAttrgroupRelationEntity;
import com.smallmq.product.service.AttrAttrgroupRelationService;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public void deleteBatchRelation(List<AttrAttrgroupRelationEntity> entities) {
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_id", entities.get(0).getAttrGroupId());
        wrapper.in("attr_id", entities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList()));
        this.remove(wrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

}