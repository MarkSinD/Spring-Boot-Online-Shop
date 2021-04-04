package com.shopme.shopmebackend.user;


import com.shopme.common.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


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
public class UserRepositiryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testCreateUser(){
        /*User user = new User("mark@mail.ru", "qwerty12345", "Mark", "Sinakaev");

        User savedUser = repository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);*/
    }
}
