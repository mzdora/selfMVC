package com.mzdora;

import com.mzdora.aop.Aop;
import com.mzdora.core.BeanContainer;
import com.mzdora.ioc.Ioc;
import com.mzdora.mvc.server.Server;
import com.mzdora.mvc.server.TomcatServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MzMVC Starter
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MzMVC {
    /**
     * 全局配置
     */
    @Getter
    private static Configuration configuration = Configuration.builder().build();

    /**
     * 默认服务器
     */
    @Getter
    private static Server server;

    /**
     * 启动
     */
    public static void run(Class<?> bootClass) {
        run(Configuration.builder().bootClass(bootClass).build());
    }

    /**
     * 启动
     */
    public static void run(Class<?> bootClass, int port) {
        run(Configuration.builder().bootClass(bootClass).serverPort(port).build());
    }

    /**
     * 启动
     */
    public static void run(Configuration configuration) {
        new MzMVC().start(configuration);
    }

    /**
     * 初始化
     */
    private void start(Configuration configuration) {
        try {
            MzMVC.configuration = configuration;
            String basePackage = configuration.getBootClass().getPackage().getName();
            BeanContainer.getInstance().loadBeans(basePackage);
            //注意Aop必须在Ioc之前执行
            new Aop().doAop();
            new Ioc().doIoc();

            server = new TomcatServer(configuration);
            server.startServer();
        } catch (Exception e) {
            log.error("MzMVC 启动失败", e);
        }
    }
}
