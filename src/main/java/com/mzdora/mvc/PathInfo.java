package com.mzdora.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PathInfo 存储http相关信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathInfo {
    /**
     * http请求方法
     */
    private String httpMethod;

    /**
     * http请求路径
     */
    private String httpPath;
}