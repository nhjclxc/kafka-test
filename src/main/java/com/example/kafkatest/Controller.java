package com.example.kafkatest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuoXianchao
 * @since 2024/03/01 15:14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class Controller {


    @GetMapping()
    public Object getAllUserList(){
        System.out.println("测试");

        return 200;
    }
}
