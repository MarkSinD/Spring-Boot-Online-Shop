package com.shopme.shopmebackend.category;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.shopmebackend.FileUploadUtil;
import com.shopme.shopmebackend.category.export.CategoryCsvExporter;
import com.shopme.shopmebackend.category.export.CategoryExcelExporter;
import com.shopme.shopmebackend.category.export.CategoryPDFExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService service;

    @GetMapping("")
    public String listAll(Model model) {
        return listByPage(1, model, "name", "asc", null);
    }

    @GetMapping("/page/{pageNum}")
    private String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
        LOGGER.info("Category. Sort Field : " + sortField);
        LOGGER.info("Category. Sort Order : " + sortDir);

        if (sortDir == null || sortDir.isEmpty()){
            sortDir = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listCategory = service.listByPage(pageInfo, pageNum, sortField, sortDir, keyword);

        long startCount = (pageNum - 1) * CategoryService.CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.CATEGORIES_PER_PAGE - 1;

        if (endCount > pageInfo.getTotalPages()) {
            endCount = pageInfo.getTotalPages();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "category/categories";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        List<Category> listCategories = service.listCategoriesUsedInForm();
        Category category = new Category();
        category.setEnabled(true);
        model.addAttribute("category", category);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return "category/category_form";
    }

    @PostMapping("/save")
    public String saveUser(Category category, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            category.setImage(fileName);
            Category savedCategory = service.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (category.getImage().isEmpty())
                category.setImage(null);
            service.save(category);
        }


        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
        return getRedirectURLtoAffectedUser(category);
    }

    private String getRedirectURLtoAffectedUser(Category category) {
        String keyword = category.getName();
        return "redirect:/categories/page/1?sortField=id&sortDir=asc&keyword=" + keyword;
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 Model model, RedirectAttributes redirectAttributes) {
        LOGGER.debug("delete category");
        try {
            service.delete(id);
            String categoryDir = "category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("mesasge", "The user ID "
                    + id + " has been deleted seccessfully");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id,
                               Model model, RedirectAttributes redirectAttributes){
        try {
            Category category = service.getCategoryByid(id);
            List<Category> listCategories = service.listAll("asc");
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
            return "/category/category_form";
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateUserEnableStatus(@PathVariable("id")Integer id,
                                         @PathVariable("status") boolean enabled,
                                         RedirectAttributes redirectAttributes){
        service.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> categoryList = service.listAll("id");
        CategoryCsvExporter exporter = new CategoryCsvExporter();
        exporter.export(categoryList, response);
    }

    @GetMapping("/export/excel")
    public void exportToXlsx(HttpServletResponse response) throws IOException {
        List<Category> listCategories = service.listAll("id");
        CategoryExcelExporter exporter = new CategoryExcelExporter();
        exporter.export(listCategories, response);
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<Category> listCategories = service.listAll("id");
        CategoryPDFExporter exporter = new CategoryPDFExporter();
        exporter.export(listCategories, response);
    }


}

