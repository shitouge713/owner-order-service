package com.owner.order.service.extend;

/**
 * 扩展基础接口：判断插件是否需要执行
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
