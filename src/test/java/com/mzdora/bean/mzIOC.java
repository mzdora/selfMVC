package com.mzdora.bean;

import com.mzdora.core.BeanContainer;
import com.mzdora.ioc.Ioc;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class mzIOC {
    @Test
    public void doIoc() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        //BeanContainer beanContainer = new BeanContainer();
        beanContainer.loadBeans("com.mzdora.bean");
        new Ioc().doIoc();
        mzController controller = (mzController) beanContainer.getBean(mzController.class);
        controller.helloForAspect();
    }
}