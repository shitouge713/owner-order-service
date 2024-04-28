package com.owner.order.service.extend;

import com.owner.order.context.CreateOrderContext;
import com.owner.order.exception.NoWarnException;
import com.owner.order.util.SpringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 通用请求插件操作
 * 1. 插件初始化操作
 * 2. 插件执行操作
 * OperatorHandler：表示扩展类对哪个接口操作
 *
 * @author sxl
 */
@Component
public abstract class CommonPluginChain<P extends OperatorHandler> {

    @Resource
    protected SpringUtils springUtils;
    //全部插件集合
    protected List<P> allPlugins;
    //当前线程查询插件
    protected final ThreadLocal<List<P>> currentPluginChainThreadLocal = new ThreadLocal<>();

    /**
     * 应用程序就绪后，将所有插件加载到allPlugins中
     */
    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        allPlugins = Collections.unmodifiableList(springUtils.getSortedBeansOfType(getPluginClass()));
    }


    /**
     * 获取插件类，如果自动获取的不准确，子类也可以复写
     *
     * @return 插件类
     */
    @SuppressWarnings("unchecked")
    public Class<P> getPluginClass() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            throw new NoWarnException("BaseOperatorPluginChain-子类未确认父类类型");
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Optional<Type> first = Arrays.stream(actualTypeArguments)
                .filter(a -> a instanceof Class<?> && OperatorHandler.class.isAssignableFrom((Class<?>) a)).findFirst();
        if (!first.isPresent()) {
            throw new NoWarnException("BaseOperatorPluginChain-子类未确认父类类型");
        }
        return (Class<P>) first.get();
    }

    /**
     * 获取当前请求的插件集合
     *
     * @return
     */
    public Optional<List<P>> getCurrentPlugin() {
        return Optional.ofNullable(currentPluginChainThreadLocal.get());
    }

    /**
     * 获取所有候选插件，供子类复写
     *
     * @return 候选插件
     */
    public List<P> getAllPlugins() {
        return allPlugins;
    }

    /**
     * 初始化插件
     */
    public void initPlugin() {
        //初始化上下文
        CreateOrderContext.initConfig();
        //初始化插件，canHandle()用来过滤符合条件，需要执行的插件
        List<P> pluginList = getAllPlugins().stream().filter(a -> a.canHandle()).collect(Collectors.toList());
        currentPluginChainThreadLocal.set(pluginList);
    }

    /**
     * 清空当前插件以及上下文的线程变量
     */
    public void clearThreadLocal() {
        currentPluginChainThreadLocal.remove();
        CreateOrderContext.clearThreadLocal();
    }

    /**
     * 遍历插件执行
     *
     * @param consumer 执行的方法
     */
    public void forEachChainConsumer(Consumer<P> consumer) {
        List<P> chain = currentPluginChainThreadLocal.get();
        if (CollectionUtils.isEmpty(chain)) {
            return;
        }
        for (P plugin : chain) {
            consumer.accept(plugin);
        }
    }

    /**
     * 遍历插件执行
     *
     * @param function 执行的方法
     */
    public <T> T forEachChainFunction(BiFunction<P, T, T> function, T defaultValue) {
        List<P> chain = currentPluginChainThreadLocal.get();
        if (CollectionUtils.isEmpty(chain)) {
            return defaultValue;
        }
        T result = defaultValue;
        for (P plugin : chain) {
            result = function.apply(plugin, result);
        }
        return result;
    }
}
