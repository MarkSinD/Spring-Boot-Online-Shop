package com.shopme.shopmebackend.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
    Brand findByName(String name);

    Long countById(Integer id);

    @Query("SELECT NEW Brand(b.id, b.name, b.logo) FROM Brand b ORDER BY b.name ASC")
    public List<Brand> findAll();
}
