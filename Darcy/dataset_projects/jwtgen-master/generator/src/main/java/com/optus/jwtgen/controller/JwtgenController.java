package com.optus.jwtgen.controller;

import com.optus.jwtgen.request.JwtPayload;
import com.optus.jwtgen.service.JwtgenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by optus on 28.10.17.
 */

@RestController
public class JwtgenController {

    @Autowired
    JwtgenService jwtgenService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from JWT generator!";
    }

    @RequestMapping(value = "/tokens", method = RequestMethod.POST)
    public String getToken(@Valid @RequestBody JwtPayload payload){
        try {
            return jwtgenService.generateToken(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
