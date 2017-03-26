package com.labunco.requestentity.model;

import java.util.List;

/**
 * @author kulabun
 * @since 3/25/17
 */
public interface Type {

    List<Type> getTypeArgs();

    String getName();

    String getQualifiedName();

    String asString();

    String asStringWithBounds();
}
