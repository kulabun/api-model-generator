package test;

import com.labunco.apimodelgenerator.annotation.ApiField;
import com.labunco.apimodelgenerator.annotation.GenerateApiModel;

@GenerateApiModel
public class Person {
    private String id;

    @ApiField
    private String firstName;

    @ApiField
    private String lastName;
}