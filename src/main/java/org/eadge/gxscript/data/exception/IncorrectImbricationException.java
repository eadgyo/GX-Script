package org.eadge.gxscript.data.exception;

import org.eadge.gxscript.data.entity.Entity;

/**
 * Created by eadgyo on 04/08/16.
 */
public class IncorrectImbricationException extends Exception
{
    private Entity entity0, entity1;

    public Entity getEntity0()
    {
        return entity0;
    }

    public Entity getEntity1()
    {
        return entity1;
    }

    public IncorrectImbricationException(Entity entity0, Entity entity1)
    {
        this.entity0 = entity0;
        this.entity1 = entity1;
    }
}
