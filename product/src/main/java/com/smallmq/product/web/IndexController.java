package com.smallmq.product.web;

import com.smallmq.product.entity.CategoryEntity;
import com.smallmq.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String Index(Model model) {
        List<CategoryEntity> cateGoryList = categoryService.getLevel1();
        model.addAttribute("categorys",cateGoryList);
        return "index";
    }
}
