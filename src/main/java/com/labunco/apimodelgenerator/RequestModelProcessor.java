package com.labunco.apimodelgenerator;

import com.google.auto.service.AutoService;
import com.labunco.apimodelgenerator.annotation.ApiField;
import com.labunco.apimodelgenerator.annotation.GenerateApiModel;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kulabun
 */
@SupportedAnnotationTypes({
        "com.labunco.apimodelgenerator.annotation.GenerateApiModel",
        "com.labunco.apimodelgenerator.annotation.ApiField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RequestModelProcessor extends AbstractProcessor {
    private Messager messager;
    private Types types;
    private Elements elements;

    @Override
    public void init(ProcessingEnvironment env) {
        messager = env.getMessager();
        types = env.getTypeUtils();
        elements = env.getElementUtils();
        super.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> targetClasses = (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(GenerateApiModel.class);
        targetClasses.forEach(this::generateSourceFile);
        return true;
    }

    private void generateSourceFile(TypeElement element) {
        ClassName className = getClassName(element);
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);
        GenerationContext ctx = new GenerationContext(className, typeBuilder);

        processApiFields(element, ctx);
        TypeSpec typeSpec = typeBuilder.build();

        String packageName = elements.getPackageOf(element).getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, String.format("Failed to generate %s", className.reflectionName()), element);
        }
    }

    private void processApiFields(TypeElement element, GenerationContext ctx) {
        List<FieldSpec> fields = element.getEnclosedElements().stream()
                .filter(it -> it.getKind().isField())
                .filter(it -> it.getAnnotation(ApiField.class) != null)
                .map(it -> createField(it, ctx))
                .collect(Collectors.toList());
        ctx.getTypeSpec().addFields(fields);

        List<MethodSpec> methods = fields.stream()
                .map(it -> createGetterSetter(it))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        ctx.getTypeSpec().addMethods(methods);
    }

    private List<MethodSpec> createGetterSetter(FieldSpec field) {
        return Arrays.asList(createGetter(field), createSetter(field));
    }

    private MethodSpec createGetter(FieldSpec field) {
        return MethodSpec.methodBuilder("get" + capitalize(field.name))
                .addModifiers(Modifier.PUBLIC)
                .returns(field.type)
                .addCode(CodeBlock.of("return this.$1N;\n", field.name))
                .build();
    }

    private MethodSpec createSetter(FieldSpec field) {
        return MethodSpec.methodBuilder("set" + capitalize(field.name))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(field.type, field.name)
                .addCode(CodeBlock.of("this.$1N = $1N;\n", field.name))
                .build();
    }

    public String capitalize(String str) {
        return str.length() == 0 ? str : Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private ClassName generateApiFieldModel(Element element, GenerationContext ctx) {
        ApiField.Model model = element.getAnnotation(ApiField.class).referenceModel();
        ClassName className = getApiFieldModelClassName(element, ctx, model);
        assertIsDeclaredType(element);

        Element typeElement = types.asElement(element.asType());
        List<FieldSpec> fields = createTypeFields(typeElement, model.fields());

        List<MethodSpec> methods = fields.stream()
                .map(it -> createGetterSetter(it))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        TypeSpec type = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addFields(fields)
                .addMethods(methods)
                .build();

        ctx.getTypeSpec().addType(type);
        return className;
    }

    private void assertIsDeclaredType(Element element) {
        if (!DeclaredType.class.isAssignableFrom(element.asType().getClass()))
            throw new IllegalArgumentException("You can't define a referenceModel for primitive type!");
    }

    private void assertIsFieldElement(Element element) {
        if (!element.getKind().isField())
            throw new IllegalArgumentException("Field element expected");
    }

    private ClassName getApiFieldModelClassName(Element element, GenerationContext ctx,
                                                ApiField.Model model) {
        ClassName fieldClassName = (ClassName) ClassName.get(element.asType());
        String classSimpleName = model.prefix() + fieldClassName.simpleName() + model.postfix();
        return ClassName.get(ctx.getClassPackageName(), ctx.getClassSimpleName(), classSimpleName);
    }

    private List<FieldSpec> createTypeFields(Element typeElement, String... fields) {
        List<String> fieldsList = Arrays.asList(fields);

        return typeElement.getEnclosedElements().stream()
                .filter(it -> it.getKind().isField())
                .filter(it -> fieldsList.contains(it.getSimpleName().toString()))
                .map(it -> createField(it, TypeName.get(it.asType())))
                .collect(Collectors.toList());
    }

    private FieldSpec createField(Element it, TypeName type) {
        return FieldSpec.builder(type, it.getSimpleName().toString(), Modifier.PRIVATE).build();
    }

    private FieldSpec createField(Element element, GenerationContext ctx) {
        assertIsFieldElement(element);

        ApiField annotation = element.getAnnotation(ApiField.class);
        switch (annotation.type()) {
            case Value:
                return createField(element, TypeName.get(element.asType()));
            case Reference:
                ClassName modelClassName = generateApiFieldModel(element, ctx);
                return createField(element, modelClassName);
            default:
                String msg = String.format("Unsupported ApiField.type value: %s", annotation.type());
                throw new IllegalArgumentException(msg);
        }
    }

    private ClassName getClassName(TypeElement typeElement) {
        ClassName className = ClassName.get(typeElement);
        GenerateApiModel annotation = typeElement.getAnnotation(GenerateApiModel.class);
        return className.peerClass(annotation.prefix() + className.simpleName() + annotation.postfix());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
