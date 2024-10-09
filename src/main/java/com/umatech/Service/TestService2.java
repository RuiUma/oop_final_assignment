package com.umatech.Service;

import com.umatech.TomcatApplication.annotation.Resource;
import com.umatech.TomcatApplication.annotation.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class TestService2 {

    @Resource
    private TestService testService;

    private final Logger logger = LogManager.getLogger(this.getClass());
    public String testMethod() {
        return testService.testMethod();
    }
}
