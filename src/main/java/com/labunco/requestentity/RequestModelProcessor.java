package com.labunco.requestentity;

import com.google.auto.service.AutoService;
import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.model.Type;
import com.labunco.requestentity.service.EntityConversionService;
import com.labunco.requestentity.service.VelocityService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author kulabun
 * @since 3/16/17
 */
@SupportedAnnotationTypes({
        "com.labunco.requestentity.annotation.RequestEntity",
        "com.labunco.requestentity.annotation.RequestField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RequestModelProcessor extends AbstractProcessor {
    private Messager messager;
    private VelocityService velocityService;
    private EntityConversionService entityConversionService;

    @Override
    public void init(ProcessingEnvironment env) {
        messager = env.getMessager();
        velocityService = new VelocityService();
        super.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> targetClasses = roundEnv.getElementsAnnotatedWith(RequestEntity.class);
        entityConversionService = new EntityConversionService(targetClasses);
        targetClasses.forEach(this::generateSourceFile);
        return true;
    }

    private void generateSourceFile(Element targetClass) {
        Type type = entityConversionService.toType(targetClass);
        write(type, getSourceFile(type));
    }

    private JavaFileObject getSourceFile(Type type) {
        try {
            return processingEnv.getFiler().createSourceFile(type.getName().getCanonicalName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(Type type, JavaFileObject sourceFile) {
        try (Writer writer = sourceFile.openWriter()) {
            velocityService.writeRequestEntity(writer, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
