package com.shopme.shopmebackend.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProsuct(){
        Brand brand = entityManager.find(Brand.class, 3);
        Category category = entityManager.find(Category.class, 6);

        Product product = new Product();
        product.setName("Legion 5 15ARH05H");
        product.setAlias("legion_5_15ARH05H");
        product.setShortDescription("A good smartphone from Lenovo");
        product.setFullDescription("This is a very good smartphone full description");
        product.setBrand(brand);
        product.setCategory(category);

        product.setPrise(556);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product save = productRepository.save(product);
        assertThat(save.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts(){
        Iterable<Product> productIterable = productRepository.findAll();
        productIterable.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        Optional<Product> product = productRepository.findById(id);
        System.out.println(product.get());
        assertThat(product.get()).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 1;
        Product product = productRepository.findById(id).get();
        product.setPrise(499);

        productRepository.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);
        assertThat(updatedProduct.getPrise()).isEqualTo(499);
    }

    @Test
    public void testDeleteProduct(){
        Integer id = 2;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);
        assertThat(!result.isPresent());
    }

    @Test
    public void testSaveProductWithImages(){
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra_image_1.png");
        product.addExtraImage("extra_image_2.png");
        product.addExtraImage("extra_image_3.png");

        Product saveProduct = productRepository.save(product);
        assertThat(saveProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testSaveProductDetail(){
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();
        product.addProductDetail("Device Memory", "128GB");
        product.addProductDetail("CPU Model", "MediaTek");
        product.addProductDetail("OS", "Android 10");

        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct.getDetails()).isNotNull();
    }

    @Test
    public void cleanProductDetail(){
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();
        product.getDetails().clear();
        productRepository.save(product);

        Product productTest = productRepository.findById(productId).get();
        assertThat(productTest.getDetails().size()).isEqualTo(0);
    }
}
