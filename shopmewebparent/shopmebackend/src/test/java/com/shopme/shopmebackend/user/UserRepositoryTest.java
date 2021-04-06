package com.shopme.shopmebackend.user;


import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;


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
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateFirstRoleWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 1);
        User mark = new User("mark@mail.com", "nam2020", "Nam", "Ha Minh");
        mark.addRole(roleAdmin);

        User savedUser = repository.save(mark);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWIthTwoRoles(){
        User userSam = new User("mark@mail.com", "mark1234", "Mark", "Sinakaev");
        Role roleEditor = new Role(1);
        Role roleAssistant = new Role(2);

        userSam.addRole(roleEditor);
        userSam.addRole(roleAssistant);

        User savedUser = repository.save(userSam);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> users = repository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    public void testGetUserById(){
        User userName = repository.findById(1).get();
        System.out.println(userName);
        assertThat(userName).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userName = repository.findById(1).get();
        userName.setEnabled(true);
        userName.setEmail("mark_sinakaev@mail.ru");

        repository.save(userName);
    }

    @Test
    public void testUpdateUserRoles(){
        User userName = repository.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);
        userName.getRoles().remove(roleEditor);
        userName.addRole(roleSalesperson);
        repository.save(userName);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        repository.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "mark@mail.ru";
        User user = repository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 12;
        Long countById = repository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }
}
