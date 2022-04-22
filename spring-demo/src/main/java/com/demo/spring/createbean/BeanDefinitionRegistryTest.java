package com.demo.spring.createbean;

import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

@Component
public class BeanDefinitionRegistryTest implements ImportBeanDefinitionRegistrar {


    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
//        rootBeanDefinition.setBeanClass(MyBean.class);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setBeanClass(MyBean.class);
        registry.registerBeanDefinition("MyBean-BeanDefinitionRegistry", beanDefinition);
    }
}
