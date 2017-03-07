package org.eadge.gxscript.data.io;

import org.eadge.gxscript.data.entity.model.base.GXEntity;

import java.util.Collection;
import java.util.Vector;

/**
 * Created by eadgyo on 05/03/17.
 *
 * Contain GXElements, associated with this group
 */
public class EGXGroup extends Vector<GXEntity>
{
    /**
     * Keeps the name of the GXGroup
     */
    private String name;

    public EGXGroup(int i, int i1, String name)
    {
        super(i, i1);
        this.name = name;
    }

    public EGXGroup(int i, String name)
    {
        super(i);
        this.name = name;
    }

    public EGXGroup(String name)
    {
        this.name = name;
    }

    public EGXGroup(Collection<? extends GXEntity> collection, String name)
    {
        super(collection);
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}