package com.umatech.Controller;

import com.umatech.TomcatApplication.annotation.RequestMapping;
import com.umatech.TomcatApplication.annotation.RestController;

@RestController("/controller")
public class HelloController {

    @RequestMapping(value = "/method", method = "POST")
    public String testMethod() {
        return "this is a message from controller";
    }
}
