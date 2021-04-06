package com.shopme.shopmebackend;

import com.shopme.shopmebackend.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Controller
public class MainController {

    @Autowired
    private RoleRepository repository;

    @GetMapping("/")
    public String viewHome(){
        return "index";
    }
}
