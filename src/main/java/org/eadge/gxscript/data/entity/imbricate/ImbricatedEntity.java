package org.eadge.gxscript.data.entity.imbricate;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Model for imbrication blocks.
 */
public abstract class ImbricatedEntity extends Entity
{
    /**
     * Check if one output is in imbrication process
     * @param index output imbrication
     * @return true if the index is pointing toward an output in imbrication, false otherwise
     */
    public abstract boolean isInImbrication(int index);

    /**
     * Get output entities that are in the imbrication of this entity
     * @return all output entities in imbrication of this entity
     */
    public abstract Collection<Entity> getImbricatedOutputs();

    /**
     * Get output entities that are NOT in the imbrication of this entity
     * @return all output entities NOT in imbrication of this entity
     */
    public abstract Collection<Entity> getNotImbricatedOutputs();
}
