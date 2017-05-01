package test;

import com.labunco.apimodelgenerator.annotation.ApiField;
import com.labunco.apimodelgenerator.annotation.GenerateApiModel;

import java.util.UUID;

@GenerateApiModel
public class Account {
    private UUID id;

    @ApiField
    private Integer amount;

    @ApiField(type = ApiField.Type.Reference, referenceModel = @ApiField.Model(fields = "code"))
    private Currency currency;

    @ApiField(type = ApiField.Type.Reference, referenceModel = @ApiField.Model(fields = "id"))
    private Person person;
}