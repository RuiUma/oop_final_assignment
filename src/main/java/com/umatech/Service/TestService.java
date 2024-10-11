package com.umatech.Service;

import com.umatech.TomcatApplication.annotation.Resource;
import com.umatech.TomcatApplication.annotation.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class TestService {
    @Resource
    private TestService2 testService2;
    private final Logger logger = LogManager.getLogger(this.getClass());
    public String testMethod() {
        logger.info("message from service");
        return "message from service";
    }
}
