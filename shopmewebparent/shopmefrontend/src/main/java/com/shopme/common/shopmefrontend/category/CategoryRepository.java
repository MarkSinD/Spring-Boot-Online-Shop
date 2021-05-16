package com.shopme.common.shopmefrontend.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;
import org.springframework.stereotype.Repository;
import javax.persistence.Entity;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
    List<Category> findAllEnabled();

    @Query("SELECT c FROM Category c WHERE c.enabled = true and c.alias = ?1 ORDER BY c.alias ASC")
    Category findByALiasEnabled(String alies);

}