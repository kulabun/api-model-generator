package com.labunco.apimodelgenerator;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * @author kulabun
 */
@RunWith(JUnit4.class)
public class ApiModelGeneratorTest {

    @Test
    public void successSimpleFields() {
        JavaFileObject source = loadResource("test/Account.java");
        JavaFileObject generatedModel = loadResource("test/AccountModel.java");

        Truth.assertAbout(javaSource())
                .that(source)
                .processedWith(new RequestModelProcessor())
                .compilesWithoutError()
                .and().generatesSources(generatedModel);
    }

    @Test
    public void successReferenceFields() {
        JavaFileObject source = loadResource("test/Person.java");
        JavaFileObject generatedModel = loadResource("test/PersonModel.java");

        Truth.assertAbout(javaSource())
                .that(source)
                .processedWith(new RequestModelProcessor())
                .compilesWithoutError()
                .and().generatesSources(generatedModel);
    }

    @Test
    public void successNoFields() {
        JavaFileObject currenySource = loadResource("test/Currency.java");
        JavaFileObject generatedModel = loadResource("test/CurrencyModel.java");
        
        Truth.assertAbout(javaSource())
                .that(currenySource)
                .processedWith(new RequestModelProcessor())
                .compilesWithoutError()
                .and().generatesSources(generatedModel);
    }

    @Test
    public void successModelList() {
        JavaFileObject currenySource = loadResource("test/Group.java");
        JavaFileObject generatedModel = loadResource("test/GroupModel.java");

        Truth.assertAbout(javaSource())
                .that(currenySource)
                .processedWith(new RequestModelProcessor())
                .compilesWithoutError()
                .and().generatesSources(generatedModel);
    }

    private JavaFileObject loadResource(String resourceName) {
        return JavaFileObjects.forResource(resourceName);
    }
}
