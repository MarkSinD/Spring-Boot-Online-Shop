package com.shopme.common.shopmefrontend;

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
    public String getIndex(){
        return "index";
    }
}
