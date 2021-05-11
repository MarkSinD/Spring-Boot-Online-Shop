package com.shopme.shopmebackend.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Service
@Transactional
public class CategoryService {

    public static final int CATEGORIES_PER_PAGE = 4;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Category> listAll(String sortDir){
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);
        Page<Category> pageCategories = null;

        if(keyword != null && !keyword.isEmpty()){
            pageCategories = categoryRepository.search(keyword, pageable);
        }
        else{
            pageCategories = categoryRepository.findRootCategories(pageable);
        }
        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if(keyword != null && !keyword.isEmpty()){
            List<Category> searchResult = pageCategories.getContent();
            for(Category category : searchResult){
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return searchResult;
        }
        else{
            return listHierarchicalCategories(rootCategories, sortDir);
        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories){
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortedSubCatefgories(rootCategory.getChildren(), sortDir);
            for (Category subCategory : children){
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir){
        Set<Category> children = sortedSubCatefgories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for(Category subCategory : children){
            StringBuilder name = new StringBuilder();
            for(int i = 0; i < subLevel; i++){
                name.append("--");
            }
            name.append(subCategory.getName());
            hierarchicalCategories.add(Category.copyFull(subCategory, name.toString()));
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }
    }


    public Category save(Category category) {
        Category parent = category.getParent();
        if(parent != null){
            String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIds);
        }
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoryListUsedInForm = new ArrayList<>();
        Iterable<Category> categories;

        categories = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for(Category category : categories) {
            if(category.getParent() == null)
                recursionTraversalCategories(category, "", categoryListUsedInForm);
        }
        return categoryListUsedInForm;
    }

    private void recursionTraversalCategories(Category category, String delimiter, List<Category> categoryListUsedInForm){
        categoryListUsedInForm.add(Category.copyFull(category, delimiter + category.getName()));
        for(Category subCategory : sortedSubCatefgories(category.getChildren())){
            if(subCategory != null)
                recursionTraversalCategories(subCategory, delimiter + "----", categoryListUsedInForm);

        }
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if(countById == null || countById == 0){
            throw new CategoryNotFoundException("Could not find any user with ID = " + id);
        }
        categoryRepository.deleteById(id);
    }

    public Category getCategoryByid(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException exception){
            throw new CategoryNotFoundException("Could not find any category with ID");
        }
    }

    public String checkUnique(Integer id, String name, String alias){
        boolean isCreatingNow = (id == null || id == 0);

        Category categoryByName = categoryRepository.findByName(name);
        if(isCreatingNow){
            if(categoryByName != null){
              return "DuplicateName";
            }else{
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if(categoryByAlias != null){
                    return "DuplicateAlias";
                }
            }
        } else{
            if(categoryByName != null && categoryByName.getId() != id){
                return "DuplicateName";
            }
            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if(categoryByAlias != null && categoryByAlias.getId() != id){
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    private SortedSet<Category> sortedSubCatefgories(Set<Category> children) {
        return sortedSubCatefgories(children, "asc");
    }

    private SortedSet<Category> sortedSubCatefgories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category category1, Category category2) {
                if (sortDir.equals("asc")) {
                    return category1.getName().compareTo(category2.getName());
                } else {
                    return category2.getName().compareTo(category1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }
}
