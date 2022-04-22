package com.demo.spring.createbean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryAwareTest implements BeanFactoryAware {

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;

        defaultListableBeanFactory.registerSingleton("MyBean-BeanFactory-registerSingleton", new MyBean());

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setBeanClass(MyBean.class);

        defaultListableBeanFactory.registerBeanDefinition("MyBean-BeanFactory-registerBeanDefinition", beanDefinition);
    }
}
