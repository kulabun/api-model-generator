package test;

import com.labunco.apimodelgenerator.annotation.ApiField;
import com.labunco.apimodelgenerator.annotation.GenerateApiModel;
import java.util.List;

import java.util.UUID;

@GenerateApiModel
public class Group {
    private UUID id;

    @ApiField(type = ApiField.Type.Reference, 
            referenceModel = @ApiField.Model(fields = "id", targetClass = Person.class))
    private List<Person> persons;
}