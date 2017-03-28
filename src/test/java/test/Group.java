package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;
import com.labunco.requestentity.annotation.RequestLink;

import java.util.List;

/**
 * @author kulabun
 */
@RequestEntity
public class Group {
    private String id;

    @RequestField
    private String name;

    @RequestLink(fields = "id")
    private List<Person> members;
}
