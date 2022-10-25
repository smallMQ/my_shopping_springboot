package com.smallmq.ware.controller;

import com.smallmq.utils.PageUtils;
import com.smallmq.utils.R;
import com.smallmq.ware.entity.PurchaseEntity;
import com.smallmq.ware.service.PurchaseService;
import com.smallmq.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 采购信息
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:29:12
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取采购单
     */
    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 领取采购单
     */
    @RequestMapping("/received")
    public R received(@RequestBody Long[] ids) {
        purchaseService.receivedPurchase(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 合并采购需求
     */
    @PostMapping("/merge")
    public R mergePurchase(@RequestBody MergeVo vo) {
        purchaseService.mergePurchase(vo);
        return R.ok();
    }
}
