package com.shopme.shopmebackend.category;

import com.shopme.shopmebackend.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CategoryRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRestController.class);

    @Autowired
    private CategoryService service;


    @PostMapping("/categories/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name")String name, @Param("alias") String alias){
        LOGGER.info("Check_unique. id : " + id + " . name : " + name + " .alias : " + alias);
        return service.checkUnique(id, name, alias);
    }
}
