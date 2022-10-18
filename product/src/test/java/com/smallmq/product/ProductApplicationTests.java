package com.smallmq.product;

import com.smallmq.product.entity.BrandEntity;
import com.smallmq.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductApplicationTests {
    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setLogo("aaa");
//        brandService.save(brandEntity);
//        System.out.println("success");

    }

}
