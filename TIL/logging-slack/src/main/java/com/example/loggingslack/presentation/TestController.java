package com.example.loggingslack.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/")
    public String test() {
        logger.error("ERROR");
        logger.info("INFO");
        logger.debug("DEBUG");
        return "test";
    }

}