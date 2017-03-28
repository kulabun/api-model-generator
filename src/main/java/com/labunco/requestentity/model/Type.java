package com.labunco.requestentity.model;

import java.util.List;

/**
 * @author kulabun
 */
public interface Type {

    List<Type> getTypeArgs();

    String getName();

    String getQualifiedName();

    String asString();

    String asStringWithBounds();
}
