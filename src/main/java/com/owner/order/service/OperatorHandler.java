package com.owner.order.service;

/**
 * 判断是否可以处理该请求
 *
 * @author sxl
 */
public interface OperatorHandler {

    /**
     * 当前插件是否可以处理该请求
     * default 默认返回true，表示扩展接口需要处理该请求
     *
     * @return 是否可以处理
     */
    default boolean canHandle() {
        return true;
    }

}
