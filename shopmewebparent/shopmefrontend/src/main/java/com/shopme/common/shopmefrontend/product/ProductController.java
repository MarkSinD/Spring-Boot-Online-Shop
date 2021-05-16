package com.shopme.common.shopmefrontend.product;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.shopmefrontend.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) throws CategoryNotFoundException {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum, Model model) throws CategoryNotFoundException {

        Category category = categoryService.getCategory(alias);
        if(category == null){
            return "error/404";
        }

        List<Category> categoryPatents = categoryService.getCategoryParents(category);
        Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
        List<Product> listProducts = pageProducts.getContent();


        long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if(endCount > pageProducts.getTotalElements()){
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("categoryPatents", categoryPatents);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("category", category);

        return "product/products_by_category";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {

        try{
            Product product = productService.getProduct(alias);
            List<Category> categoryPatents = categoryService.getCategoryParents(product.getCategory());
            model.addAttribute("categoryPatents", categoryPatents);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getShortName());


            return "product/product_detail";
        } catch (ProductNotFoundException e){
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword")String keyword, Model model){
        return searchByPage(keyword, 1, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword")String keyword,
                         @PathVariable("pageNum") int pageNum,
                         Model model){

        Page<Product> pageProducts = productService.search(keyword, pageNum);
        List<Product> listResult = pageProducts.getContent();

        model.addAttribute("keyword", keyword);
        model.addAttribute("listResult", listResult);

        long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
        if(endCount > pageProducts.getTotalElements()){
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Result");

        return "product/search_result";
    }
}
