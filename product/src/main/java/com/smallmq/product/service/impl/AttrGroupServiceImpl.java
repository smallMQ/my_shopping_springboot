package com.smallmq.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.product.dao.AttrAttrgroupRelationDao;
import com.smallmq.product.dao.AttrDao;
import com.smallmq.product.dao.AttrGroupDao;
import com.smallmq.product.entity.AttrAttrgroupRelationEntity;
import com.smallmq.product.entity.AttrEntity;
import com.smallmq.product.entity.AttrGroupEntity;
import com.smallmq.product.service.AttrGroupService;
import com.smallmq.product.vo.AttrGroupVo;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    /**
     * 根据分类id查出所有的分组,以及分组属性
     * @param catelogId
     * @return
     */
    @Transactional
    @Override
    public List<AttrGroupVo> getAttrGroupWithAttrs(Long catelogId) {
        // 查询属性组表中的和catelogId一样的组
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id",catelogId);
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(wrapper);
        // 查询关系表
        List<AttrGroupVo> attrGroupVos = attrGroupEntities.stream().map((item) -> {
            AttrGroupVo attrGroupVo = new AttrGroupVo();
            BeanUtils.copyProperties(item, attrGroupVo);
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("attr_group_id", item.getAttrGroupId());
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(wrapper1);
            List<Long> attrIds = attrAttrgroupRelationEntities.stream().map((item2) -> {
                return item2.getAttrId();
            }).collect(Collectors.toList());
            if(attrIds != null && attrIds.size() > 0){
                // 查询属性表
                List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
                attrGroupVo.setAttrs(attrEntities);
            }


            return attrGroupVo;
        }).collect(Collectors.toList());

        return attrGroupVos;
    }

    @Override
    public PageUtils getAttrNoRelation(Map<String, Object> params, Long attrgroupId) {
        // 1.当前分组只能关联自己所属分类里面的所有属性
        AttrGroupEntity attrGroupEntity = this.getById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        // 2.当前分组只能关联别的分组没有引用的属性
        // 2.1 当前分类下的其他分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> collect = attrGroupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        // 2.2 这些分组关联的属性
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        // 2.3 从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", 1);
        if (attrIds != null && attrIds.size() > 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (key != null && key.length() > 0) {
            wrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = attrDao.selectPage(new Query<AttrEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrRelation(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attr_group_id = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        // 查出所有的属性id
        List<Long> attrIds = attr_group_id.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        // 判断是否为空
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }
        // 查出所有的属性
        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
        return attrEntities;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if(key != null){
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if(catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }else{
            wrapper.eq("catelog_id", catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

}