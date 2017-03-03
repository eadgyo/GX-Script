package org.eadge.gxscript.tools.check;

import org.eadge.gxscript.data.entity.base.GXEntity;
import org.eadge.gxscript.tools.Tools;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Check if the l
 */
public class GXLiaisonChecker
{
    /**
     * Check if a connection can added to GXEntity
     * @param onOutputGXEntity GXEntity linked on output
     * @param indexOfOutputEntity index on output
     * @param onInputGXEntity GXEntity linked on script
     * @param indexOfInputEntity index on script
     * @return true if connection can be made, false otherwise
     */
    public static boolean canConnect(GXEntity onOutputGXEntity, int indexOfOutputEntity, GXEntity onInputGXEntity, int indexOfInputEntity)
    {
        // If they have no matching intput/output types
        return Tools.isEqualOrDerivedFrom(onOutputGXEntity.getOutputClass(indexOfOutputEntity), onInputGXEntity.getInputClass(indexOfInputEntity));
    }
}
