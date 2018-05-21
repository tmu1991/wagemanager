package com.wz.wagemanager.tools;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author WindowsTen
 * @date 2018/5/9 17:19
 * @description
 */
@Component
public class BeanUtils implements ApplicationContextAware,DisposableBean {

    /**
     * 当前IOC
     *
     */
    private static ApplicationContext applicationContext;

    /**
     * * 设置当前上下文环境，此方法由spring自动装配
     *
     */
    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        applicationContext = context;
    }

    /**
     * 从当前IOC获取bean
     *
     * @param beanId
     * bean的id
     * @return
     *
     */
    public static Object getObject(String beanId) {
        assertContextInjected ();
        return applicationContext.getBean(beanId);
    }

    @Override
    public void destroy () throws Exception {
        applicationContext = null;
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        if(applicationContext == null) {
            throw new IllegalStateException("applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
        }
    }
}
