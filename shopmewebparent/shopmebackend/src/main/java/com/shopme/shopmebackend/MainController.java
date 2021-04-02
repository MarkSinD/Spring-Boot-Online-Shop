package com.shopme.shopmebackend;

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
    @GetMapping("/")
    public String viewHome(){
        System.out.println("YES!");
        return "index";
    }
}
