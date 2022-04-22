package com.demo.spring.createbean;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

@Component
public class BeanImportSelector implements ImportSelector {

    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        return new String[]{BeanImportSelector.class.getName()};
    }


    class BeanImport {

    }
}
