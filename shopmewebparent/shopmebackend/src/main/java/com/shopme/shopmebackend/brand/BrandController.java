package com.shopme.shopmebackend.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.shopmebackend.FileUploadUtil;
import com.shopme.shopmebackend.category.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Controller
@RequestMapping("/brands")
public class BrandController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String listAll(Model model, RedirectAttributes redirectAttributes){
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);
        redirectAttributes.addFlashAttribute("message", "piska");
        return "brand/brand";
    }

    @GetMapping("/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        Brand brand = new Brand();
        model.addAttribute("brand", brand);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return "brand/brand_form";
    }

    @PostMapping("/save")
    public String saveUser(Brand brand, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            // Take name
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            //
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);
            String uploadDir = "brand-logo/" + savedBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (brand.getLogo().isEmpty())
                brand.setLogo(null);
            brandService.save(brand);
        }


        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
        return getRedirectURLtoAffectedUser(brand);
    }

    private String getRedirectURLtoAffectedUser(Brand brand) {
        String keyword = brand.getName();
        return "redirect:/brands/";
        //return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=" + keyword;
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try{
            Brand brand = brandService.getBrandById(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("brand", brand);
            model.addAttribute("pageTitle", "Edit Brand  (ID: " + id + ")");
            return "brand/brand_form";
        }catch (BrandNotFoundException e){
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect/brand";

        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes){
        try{
            brandService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The brand ID "
                    + id + " has been deleted successfully");
        } catch (BrandNotFoundException e){
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/brands";
    }




}
