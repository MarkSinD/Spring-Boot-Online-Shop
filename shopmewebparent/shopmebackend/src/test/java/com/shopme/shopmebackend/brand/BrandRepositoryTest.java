package com.shopme.shopmebackend.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateFirstBrandWithOneCategory(){
        Category category1 = entityManager.find(Category.class, 6);
        Brand brand = new Brand("Acer", "default.png");
        brand.addCategory(category1);

        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrandWithTwoCategory(){
        Category category1 = entityManager.find(Category.class, 4);
        Category category2 = entityManager.find(Category.class, 7);
        Brand brand = new Brand("Apple", "default.png");
        brand.addCategory(category1);
        brand.addCategory(category2);

        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrandWithThreeCategory(){
        Category category1 = entityManager.find(Category.class, 29);
        Category category2 = entityManager.find(Category.class, 24);
        Brand brand = new Brand("Sumsung", "default.png");
        brand.addCategory(category1);
        brand.addCategory(category2);

        Brand savedBrand = brandRepository.save(brand);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllBrands(){
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);
    }

    @Test
    public void testGetBrandById(){
        Brand brand = null;
        Optional<Brand> optionalBrand = brandRepository.findById(1);
        if(optionalBrand.isPresent())
            brand = optionalBrand.get();
        System.out.println(brand);
        assertThat(brand).isNotNull();
    }

    @Test
    public void testUpdateBrandDetailsFirst(){
        Brand brand = brandRepository.findById(3).get();
        brand.setName("Samsung Electronics");
        brandRepository.save(brand);
    }

    @Test
    public void testUpdateBrandDetailsSecond(){
        Brand brand = brandRepository.findById(2).get();
        brand.setName("Acer");
        brandRepository.save(brand);
    }

    @Test
    public void testDeleteBrand(){
        Integer brandId = 2;
        brandRepository.deleteById(brandId);

        Optional<Brand> result = brandRepository.findById(brandId);
        assertThat(result.isPresent());
    }























}