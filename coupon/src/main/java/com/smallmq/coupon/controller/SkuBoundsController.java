package com.smallmq.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smallmq.coupon.entity.SkuBoundsEntity;
import com.smallmq.coupon.service.SkuBoundsService;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.R;


/**
 * 商品sku积分设置
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:13:17
 */
@RestController
@RequestMapping("coupon/skubounds")
public class SkuBoundsController {
    @Autowired
    private SkuBoundsService skuBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SkuBoundsEntity skuBounds = skuBoundsService.getById(id);

        return R.ok().put("skuBounds", skuBounds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuBoundsEntity skuBounds) {
        skuBoundsService.save(skuBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuBoundsEntity skuBounds) {
        skuBoundsService.updateById(skuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        skuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
