package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.base.GXEntity;
import org.eadge.gxscript.data.entity.script.ScriptGXEntity;
import org.eadge.gxscript.data.script.CompiledGXScript;

/**
 * Created by eadgyo on 28/02/17.
 *
 * Used to create GXEntity object from scripts
 */
public class GXEntityCreator
{
    /**
     * Create GXEntity from GXScript
     *
     * @param compiledGXScript used RawGXScript
     *
     * @return GXEntity from raw GXScript
     */
    public GXEntity createGXEntity(final CompiledGXScript compiledGXScript)
    {
        // Create the entity
        return new ScriptGXEntity(compiledGXScript);
    }
}
