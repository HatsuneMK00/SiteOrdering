package xyz.st.meethere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/hello")
    String hello(){
        return "hello";
    }
}
