package com.smallmq.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.product.dao.AttrDao;
import com.smallmq.product.dao.CategoryDao;
import com.smallmq.product.entity.AttrAttrgroupRelationEntity;
import com.smallmq.product.entity.AttrEntity;
import com.smallmq.product.service.AttrAttrgroupRelationService;
import com.smallmq.product.service.AttrService;
import com.smallmq.product.vo.AttrResVo;
import com.smallmq.product.vo.AttrVo;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (key != null) {
            wrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResVo> resVoList = records.stream().map((item) -> {
            AttrResVo attrResVo = new AttrResVo();
            BeanUtils.copyProperties(item, attrResVo);
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", item.getAttrId()));
            if (attrAttrgroupRelationEntity != null) {
                attrResVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
            }
            attrResVo.setCatelogName(categoryDao.selectById(item.getCatelogId()).getName());
            return attrResVo;
        }).collect(Collectors.toList());
        pageUtils.setList(resVoList);
        return pageUtils;
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 保存基本数据
        this.save(attrEntity);
        // 保存关联关系
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);

    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

}