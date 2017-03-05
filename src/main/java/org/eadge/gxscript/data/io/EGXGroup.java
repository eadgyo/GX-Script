package org.eadge.gxscript.data.io;

import org.eadge.gxscript.data.entity.model.base.GXEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by eadgyo on 05/03/17.
 *
 * Contain GXElements, associated with this group
 */
public class EGXGroup implements Serializable
{
    /**
     * Keeps the name of the GXGroup
     */
    private String name;

    /**
     * Holds all elements
     */
    private Collection<? extends GXEntity> elements;
}