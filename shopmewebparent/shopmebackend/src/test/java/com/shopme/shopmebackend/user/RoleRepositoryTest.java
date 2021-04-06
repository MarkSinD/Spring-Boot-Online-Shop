package com.shopme.shopmebackend.user;


import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin", "manage everything");
        Role savedRole = repository.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role roleSalesperson = new Role("Saleperson", "manage product price, "
                + "customers, shipping, orders and sales report");
        Role roleEditor = new Role("Editor", "manage categories, brands, "
                + "products, articles and menus");
        Role roleShipper = new Role("Shipper", "view products, view orders"
                + "and update order status");
        Role roleAssistant = new Role("Assistant", "manage questions and reviews");

        List<Role> roleList = new ArrayList<>();
        roleList.add(roleAssistant);
        roleList.add(roleEditor);
        roleList.add(roleSalesperson);
        roleList.add(roleShipper);
        
        repository.saveAll(roleList);
    }
}
