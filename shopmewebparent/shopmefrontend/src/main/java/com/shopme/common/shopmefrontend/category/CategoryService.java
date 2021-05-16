package com.shopme.common.shopmefrontend.category;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listNoChildrenCategories(){
        List<Category> listNoChildrenCategories = new ArrayList<>();
        List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if(children == null || children.size() == 0){
                listNoChildrenCategories.add(category);
            }
        });
        return listNoChildrenCategories;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByALiasEnabled(alias);
        if(category == null){
            throw new CategoryNotFoundException("Could not find any categories with alias " + alias);
        }

        return category;
    }

    public List<Category> getCategoryParents(Category child){
        List<Category> listParents = new ArrayList<>();

        Category parent = child.getParent();

        while(parent != null){
            listParents.add(0, parent);
            parent = parent.getParent();
        }

        listParents.add(child);
        return listParents;
    }
}
