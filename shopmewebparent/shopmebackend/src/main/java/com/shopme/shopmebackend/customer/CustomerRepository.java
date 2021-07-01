package com.shopme.shopmebackend.customer;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

    @Query("SELECT u FROM Customer u WHERE u.email = :email")
    Customer getCustomerByEmail(@Param("email") String email);

    Long countById(Integer id);

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
            + "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, "
            + "' ', c.postalCode, ' ', c.country.name) LIKE %?1%")
   Page<Customer> findAll(String keyword, Pageable pageable);

    @Query("UPDATE Customer u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

}
