package com.shopme.common.shopmefrontend.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


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

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testListEnabledCategories(){
        List<Category> categoryList = repository.findAllEnabled();
        categoryList.forEach(category -> {
            System.out.println(category.getName() + " (" + category.isEnabled() + ")");
        });
    }

    @Test
    public void testFindCategoryByAlias(){
        String alias = "electronics";
        Category category = repository.findByALiasEnabled(alias);

        assertThat(category).isNotNull();
    }

    @Test
    public void testCategoryParentsMethod(){
        Category category = repository.findById(17).get();
        List<Category> categoryParents = categoryService.getCategoryParents(category);
        System.out.println(categoryParents);
    }


}
