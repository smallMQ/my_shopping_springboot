package com.smallmq.product.controller;

import com.smallmq.product.entity.SkuInfoEntity;
import com.smallmq.product.service.SkuInfoService;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * sku信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 19:29:27
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
//        PageUtils page = skuInfoService.queryPage(params);
        PageUtils page = skuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
