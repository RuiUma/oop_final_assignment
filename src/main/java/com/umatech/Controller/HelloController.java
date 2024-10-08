package com.umatech.Controller;

import com.umatech.TomcatApplication.AutoScan.RequestMapping;
import com.umatech.TomcatApplication.AutoScan.RestController;

@RestController("/controller")
public class HelloController {

    @RequestMapping(value = "/method", method = "POST")
    public String testMethod() {
        return "this is a message from controller";
    }
}
