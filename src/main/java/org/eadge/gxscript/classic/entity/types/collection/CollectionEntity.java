package org.eadge.gxscript.classic.entity.types.collection;

import org.eadge.gxscript.data.entity.DefaultVariableEntity;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Collection entity
 */
public abstract class CollectionEntity extends DefaultVariableEntity
{
    public abstract Class getItemClass();
}
