package com.owner.order.context;


import com.owner.order.vo.OrderReqVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 订单插件上下文
 *
 * @author sxl
 */
public class CreateOrderContext {

    //线程变量
    private static final ThreadLocal<Object> simpleItemConfigDtoThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    /**
     * 初始化插件的上下文
     */
    public static void initConfig(OrderReqVO vo) {
        simpleItemConfigDtoThreadLocal.set(vo);
        context.set(new HashMap<>());
    }

    /**
     * 清空threadLocal
     */
    public static void clearThreadLocal() {
        simpleItemConfigDtoThreadLocal.remove();
        context.remove();
    }

    /**
     * 向上下文中添加数据
     *
     * @param key key
     * @param obj obj
     */
    public static void putIntoContext(String key, Object obj) {
        context.get().put(key, obj);
    }

    /**
     * 从上下文中取数据
     *
     * @param key key
     * @return key对应的数据
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getFromContext(String key) {
        return (Optional<T>) Optional.ofNullable(context.get().get(key));
    }

    /**
     * 从上下文中取数据,当确认一定存在的时候，可以使用这个方法，不返回Optional
     *
     * @param key key
     * @return key对应的数据
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCertainlyExistFromContext(String key) {
        return (T) context.get().get(key);
    }

    /**
     * 从上下文中取数据,没有则使用supplier创建对象，并设置到上线文中
     *
     * @param key      key
     * @param supplier 上下文中没有对象时，使用的构造器
     * @param <T>      数据类型泛型
     * @return key对应的数据
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFromContext(String key, Supplier<T> supplier) {
        Object o = context.get().get(key);
        if (o == null) {
            T t = supplier.get();
            context.get().put(key, t);
            return t;
        } else {
            return (T) o;
        }
    }
}
