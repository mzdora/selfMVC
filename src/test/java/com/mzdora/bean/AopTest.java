package com.mzdora.bean;

import com.mzdora.aop.Aop;
import com.mzdora.core.BeanContainer;
import com.mzdora.ioc.Ioc;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class AopTest {
    @Test
    public void doAop() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.mzdora");
        new Aop().doAop();
        new Ioc().doIoc();
        mzController controller = (mzController) beanContainer.getBean(mzController.class);
        controller.hello();
        controller.helloForAspect();
    }
}