package com.shopme.common.shopmefrontend;

import com.shopme.common.entity.Category;
import com.shopme.common.shopmefrontend.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Category> categoryList = categoryService.listNoChildrenCategories();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }
}
