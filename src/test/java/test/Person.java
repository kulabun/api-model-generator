package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

/**
 * @author kulabun
 * @since 3/16/17
 */
@RequestEntity
public class Person {
    @RequestField
    private String firstName;

    @RequestField
    private String lastName;
}
