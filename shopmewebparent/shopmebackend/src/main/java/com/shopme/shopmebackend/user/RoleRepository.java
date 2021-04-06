package com.shopme.shopmebackend.user;

import com.shopme.common.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
