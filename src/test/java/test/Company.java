package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

import java.util.List;

/**
 * @author kulabun
 */
@RequestEntity
public class Company {
    @RequestField
    private String name;

    @RequestField
    private String phone;

    @RequestField
    private Person director;

    @RequestField
    private List<Person> employees;
}
