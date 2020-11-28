package com.mzdora.mvc;

import com.alibaba.fastjson.JSON;
import com.mzdora.core.BeanContainer;
import com.mzdora.mvc.annotation.ResponseBody;
import com.mzdora.mvc.bean.ModelAndView;
import com.mzdora.util.CastUtil;
import com.mzdora.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 结果执行器
 */
@Slf4j
public class ResultRender {

    private BeanContainer beanContainer;

    public ResultRender() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * 执行Controller的方法
     * 通过调用类中的invokeController()方法反射调用了Controller中的方法并根据结果解析对应的页面。主要流程为：
     *
     * 调用getRequestParams() 获取HttpServletRequest中参数
     * 调用instantiateMethodArgs() 实例化调用方法要传入的参数值
     * 通过反射调用目标controller的目标方法
     * 调用resultResolver()解析方法的返回值，选择返回页面或者json
     */
    public void invokeController(HttpServletRequest req, HttpServletResponse resp, ControllerInfo controllerInfo) {
        // 1. 获取HttpServletRequest所有参数
        Map<String, String> requestParam = getRequestParams(req);
        // 2. 实例化调用方法要传入的参数值
        List<Object> methodParams = instantiateMethodArgs(controllerInfo.getMethodParameter(), requestParam);

        Object controller = beanContainer.getBean(controllerInfo.getControllerClass());
        Method invokeMethod = controllerInfo.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result;
        // 3. 通过反射调用方法
        try {
            if (methodParams.size() == 0) {
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 4.解析方法的返回值，选择返回页面或者json
        resultResolver(controllerInfo, result, req, resp);
    }

    /**
     * 获取http中的参数
     */
    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        //GET和POST方法是这样获取请求参数的
        request.getParameterMap().forEach((paramName, paramsValues) -> {
            if (ValidateUtil.isNotEmpty(paramsValues)) {
                paramMap.put(paramName, paramsValues[0]);
            }
        });
        // TODO: Body、Path、Header等方式的请求参数获取
        return paramMap;
    }

    /**
     * 实例化方法参数
     */
    private List<Object> instantiateMethodArgs(Map<String, Class<?>> methodParams, Map<String, String> requestParams) {
        return methodParams.keySet().stream().map(paramName -> {
            Class<?> type = methodParams.get(paramName);
            String requestValue = requestParams.get(paramName);
            Object value;
            if (null == requestValue) {
                value = CastUtil.primitiveNull(type);
            } else {
                value = CastUtil.convert(type, requestValue);
                // TODO: 实现非原生类的参数实例化
            }
            return value;
        }).collect(Collectors.toList());
    }


    /**
     * Controller方法执行后返回值解析
     */
    private void resultResolver(ControllerInfo controllerInfo, Object result, HttpServletRequest req, HttpServletResponse resp) {
        if (null == result) {
            return;
        }
        boolean isJson = controllerInfo.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            // 设置响应头
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            // 向响应中写入数据
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(JSON.toJSONString(result));
                writer.flush();
            } catch (IOException e) {
                log.error("转发请求失败", e);
                // TODO: 异常统一处理，400等...
            }
        } else {
            String path;
            if (result instanceof ModelAndView) {
                ModelAndView mv = (ModelAndView) result;
                path = mv.getView();
                Map<String, Object> model = mv.getModel();
                if (ValidateUtil.isNotEmpty(model)) {
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
            } else if (result instanceof String) {
                path = (String) result;
            } else {
                throw new RuntimeException("返回类型不合法");
            }
            try {
                req.getRequestDispatcher("/templates/" + path).forward(req, resp);
            } catch (Exception e) {
                log.error("转发请求失败", e);
                // TODO: 异常统一处理，400等...
            }
        }
    }
}