package org.eadge.gxscript.data.entity.imbricate;

import org.eadge.gxscript.data.entity.Entity;

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
}
