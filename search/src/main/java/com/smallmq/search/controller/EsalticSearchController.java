package com.smallmq.search.controller;

import com.smallmq.search.service.EsalticSearchService;
import com.smallmq.to.es.SkuEsModel;
import com.smallmq.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/search/save")
public class EsalticSearchController {

    @Autowired
    private EsalticSearchService esalticSearchService;


    // 上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = false;
        try {
            b = esalticSearchService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error(400, "商品上架错误");
        }
        if (!b) {
            return R.ok();
        } else {
            return R.error(400, "商品上架错误");
        }
    }
}
