package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping
    public String testController(){
        return "Hello World!";
    }

    @GetMapping("/testRequestBody")
    public ResponseDTO<String> testControllerRequestBody(){
        List<String> list = new ArrayList<>();
        list.add("Hello world! ResponseDto");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }

}
