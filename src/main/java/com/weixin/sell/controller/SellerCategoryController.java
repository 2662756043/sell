package com.weixin.sell.controller;

import com.weixin.sell.entity.ProductCategory;
import com.weixin.sell.exception.SellException;
import com.weixin.sell.form.CategoryForm;
import com.weixin.sell.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public String list(Model model) {
        List<ProductCategory> categoryList =  productCategoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "category/list";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                        Model model) {
        if (categoryId != null) {
            ProductCategory category = productCategoryService.findOne(categoryId);
            model.addAttribute("category",category);
        }

        return "category/index";
    }

    @PostMapping("/save")
    public String save(@Valid CategoryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("msg", bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("url", "/sell/seller/category/index");
            return "common/error";
        }
        try {
            ProductCategory productCategory = null;
            // 如果categoryId为空说明是新增
            if (form.getCategoryId() == null) {
                productCategory = new ProductCategory();
                // productCategory的id是自增的，所以无需手动设置
                // 否则是更新，需要先查询再更新
            } else {
                productCategory = productCategoryService.findOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form, productCategory);
            productCategoryService.save(productCategory);
        } catch (SellException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("url", "/sell/seller/category/index");
            return "common/error";
        }
        model.addAttribute("url","/sell/seller/category/list");
        return "common/success";
    }
}
