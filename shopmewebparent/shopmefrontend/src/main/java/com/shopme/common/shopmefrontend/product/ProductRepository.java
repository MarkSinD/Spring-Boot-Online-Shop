package com.shopme.common.shopmefrontend.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true " +
            " AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)" +
            " ORDER BY p.name ASC")
    Page<Product> listByCategory(Integer categoryID, String cateforyIDMatch, Pageable pageable);

    Product findByAlias(String alias);

    @Query(value = "SELECT * FROM products WHERE enabled = true AND "
            + "MATCH(name, short_description, full_description) AGAINST (?1)",
            nativeQuery = true)
    Page<Product> search(String keyword, Pageable pageable);
}
