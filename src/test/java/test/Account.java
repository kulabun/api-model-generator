package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;
import com.labunco.requestentity.annotation.RequestLink;

import java.util.UUID;

/**
 * @author kulabun
 */
@RequestEntity
public class Account {
    private UUID id;

    @RequestField
    private Integer amount;

    @RequestLink(fields = "code")
    private Currency currency;

    @RequestLink(fields = "id")
    private Person person;
}
