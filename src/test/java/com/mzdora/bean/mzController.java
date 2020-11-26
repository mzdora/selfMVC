package com.mzdora.bean;

import com.mzdora.ioc.annotation.Autowired;
import com.mzdora.core.annotation.Controller;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class mzController {
    @Autowired
    private mzService mzService;

    public void hello() {
        log.info(mzService.helloWord());
    }
    public void helloForAspect() { log.info("Hello Aspectj");
    }
}