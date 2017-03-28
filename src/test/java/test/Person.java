package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

/**
 * @author kulabun
 */
@RequestEntity
public class Person {
    private String id;
    
    @RequestField
    private String firstName;

    @RequestField
    private String lastName;
}
