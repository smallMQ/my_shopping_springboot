package com.smallmq.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;

import com.smallmq.product.dao.CategoryDao;
import com.smallmq.product.entity.CategoryEntity;
import com.smallmq.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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