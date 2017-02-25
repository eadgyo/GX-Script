package org.eadge.gxscript.tools.check;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.tools.Tools;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Check if the l
 */
public class GXLiaisonChecker
{
    /**
     * Check if a connection can added to entity
     * @param onOutputEntity entity linked on output
     * @param indexOfOutputEntity index on output
     * @param onInputEntity entity linked on input
     * @param indexOfInputEntity index on input
     * @return true if connection can be made, false otherwise
     */
    public static boolean canConnect(Entity onOutputEntity, int indexOfOutputEntity, Entity onInputEntity, int indexOfInputEntity)
    {
        // If they have no matching intput/output types
        return Tools.isEqualOrDerivedFrom(onOutputEntity.getOutputClass(indexOfOutputEntity), onInputEntity.getInputClass(indexOfInputEntity));
    }
}
