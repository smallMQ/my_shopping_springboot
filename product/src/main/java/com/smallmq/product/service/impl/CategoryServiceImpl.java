package com.smallmq.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.product.dao.CategoryDao;
import com.smallmq.product.entity.CategoryEntity;
import com.smallmq.product.service.CategoryBrandRelationService;
import com.smallmq.product.service.CategoryService;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public List<CategoryEntity> getLevel1() {
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryEntity::getCatId,0);
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(wrapper);
        return categoryEntities;

    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> list = new ArrayList<>();
        findParentPath(catelogId, list);
        list.add(catelogId);
        return list.toArray(new Long[list.size()]);
    }

    private void findParentPath(Long catelogId, List<Long> list) {
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            // 左侧添加
            list.add(0, byId.getParentCid());
            findParentPath(byId.getParentCid(), list);
        }
        return;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO Auto-generated method stub
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 2.组装成父子的树形结构
        // 2.1)找到所有的一级分类
        List<CategoryEntity> level1 = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0).map((menu) -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return level1;
    }

    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildren(CategoryEntity menu, List<CategoryEntity> entities) {
        List<CategoryEntity> list = entities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == menu.getCatId();
        }).map(categoryEntity -> {
            categoryEntity.setChildren(getChildren(categoryEntity, entities));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

}