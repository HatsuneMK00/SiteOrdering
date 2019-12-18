package xyz.st.meethere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{username}")
    @ResponseBody
    User getUser(@PathVariable("username") String name){
        User user = userService.getUserByName(name);
        return user;
    }

    @GetMapping("/hello")
    String hello(){
        return "hello world";
    }
}
