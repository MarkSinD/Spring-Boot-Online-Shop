package com.shopme.shopmebackend.controller;

import com.shopme.shopmebackend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */

@RestController
public class UserRestController {
    @Autowired
    private UserService service;

    @PostMapping("/users/check_email")
    public String checkDulicateEmail(@Param("id") Integer id, @Param("email")String email){
        return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
