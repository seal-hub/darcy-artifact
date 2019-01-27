package com.optus.jwtgen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sbulavin on 09-Nov-2017.
 */

@RestController
public class TestSleepController {

    @RequestMapping(value = "/testsleep", method = RequestMethod.POST)
    public void getToken(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
