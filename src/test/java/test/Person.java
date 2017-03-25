package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

/**
 * Created by klabun on 3/16/17.
 */
@RequestEntity
public class Person {
    @RequestField
    private String firstName;

    @RequestField
    private String lastName;
}
