package com.smallmq.product.feign;

import com.smallmq.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("ware")
public interface WareFeignService {
    @RequestMapping("/ware/waresku/hasstock")
    public List<SkuHasStockVo> getSkuHasStock(@RequestBody List<Long> skuIds);
}
