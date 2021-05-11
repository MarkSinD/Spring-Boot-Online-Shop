package com.shopme.shopmebackend.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Computers");
        Category savedCategory = repository.save(category);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Category parent = repository.findById(4).get();
        Category cameras = new Category("Cameras", parent);
        Category smartphones = new Category("Smartphones", parent);
        List<Category> list = new ArrayList<>();
        list.add(cameras);
        list.add(smartphones);
        repository.saveAll(list);
    }

    @Test
    public void testCreateOneCategory(){
        Category electronics = new Category("Electronics");
        repository.save(electronics);
        assertThat(electronics.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateOneWithParentCategory(){
        Category parent = repository.findById(4).get();
        Category memory = new Category("Memory", parent);
        Category savedCategory = repository.save(memory);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){
        Category category = repository.findById(1).get();
        System.out.println("Parent category : " + category.getName());
        System.out.println("************ Children ************");

        Set<Category> children = category.getChildren();
        for (Category subcategory : children){
            System.out.println("id : " + subcategory.getId() + "    name : " + subcategory.getName());
        }
        System.out.println("**********************************");
        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = repository.findAll();

        for(Category category : categories) {
            if(category.getParent() == null)
                recursionTraversalCategories(category, "");
        }
    }
    private void recursionTraversalCategories(Category category, String delimiter){
        System.out.println(delimiter + category.getName());

        for(Category subCategory : category.getChildren()){
            if(subCategory != null)
                recursionTraversalCategories(subCategory, delimiter + "---");
        }
    }

    @Test
    public void testListRootCatefories(){
        List<Category> categoryList = repository.findRootCategories(Sort.by("name").ascending());
        categoryList.forEach(cat -> System.out.println(cat.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computers";
        Category category = repository.findByName(name);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String alias = "Computers";
        Category category = repository.findByAlias(alias);
        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }

}
