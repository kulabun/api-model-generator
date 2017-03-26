package com.labunco.requestentity;

import com.google.auto.service.AutoService;
import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.model.Clazz;
import com.labunco.requestentity.service.EntityConversionService;
import com.labunco.requestentity.service.ImportsResolver;
import com.labunco.requestentity.service.VelocityService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

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
    private ImportsResolver importsResolver;
    private EntityConversionService entityConversionService;

    @Override
    public void init(ProcessingEnvironment env) {
        messager = env.getMessager();
        velocityService = new VelocityService();
        importsResolver = new ImportsResolver();
        entityConversionService = new EntityConversionService();
        super.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> targetClasses = (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(RequestEntity.class);
        targetClasses.forEach(this::generateSourceFile);
        return true;
    }

    private void generateSourceFile(TypeElement targetClass) {
        Clazz clazz = entityConversionService.toClazz(targetClass);
        List<String> imports = importsResolver.resolve(clazz);
        write(imports, clazz, getSourceFile(clazz));
    }

    private JavaFileObject getSourceFile(Clazz clazz) {
        try {
            return processingEnv.getFiler().createSourceFile(clazz.getName().getQualifiedName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(List<String> imports, Clazz clazz, JavaFileObject sourceFile) {
        try (Writer writer = sourceFile.openWriter()) {
            velocityService.writeRequestEntity(writer, imports, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
