package com.mzdora.App;

import com.mzdora.core.annotation.Controller;
import com.mzdora.mvc.annotation.RequestMapping;
import com.mzdora.mvc.annotation.ResponseBody;

@Controller
@RequestMapping
public class MzMVCController {
    @RequestMapping
    @ResponseBody
    public String hello(){
        return "Hello World!";
    }
}
