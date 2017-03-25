package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

/**
 * Created by klabun on 3/16/17.
 */
@RequestEntity
public class Company {
    @RequestField
    private String name;

    @RequestField
    private String phone;
    
    @RequestField
    private Person director;
}
