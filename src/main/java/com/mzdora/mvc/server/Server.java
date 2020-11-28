package com.mzdora.mvc.server;

/**
 * 服务器 interface
 */
public interface Server {
    /**
     * 启动服务器
     */
    void startServer() throws Exception;

    /**
     * 停止服务器
     */
    void stopServer() throws Exception;
}