package com.mzdora.bean;

import com.mzdora.core.annotation.Service;

@Service
public class mzServiceImpl implements mzService{
    @Override
    public String helloWord() {
        return "Hello World!";
    }
}
