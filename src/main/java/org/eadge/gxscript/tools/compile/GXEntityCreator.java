package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Func;

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
    public GXEntity createGXEntity(CompiledGXScript compiledGXScript)
    {
        // Create the entity
        return new DefaultGXEntity()
        {
            GXEntity init(CompiledGXScript compiledGXScript)
            {
                Class[] classParameters = compiledGXScript.getInputsScriptClasses();

                // Add used parameters as inputs
                for (int classIndex = 0; classIndex < classParameters.length; classIndex++)
                {
                    addInputEntry(classParameters[classIndex].getName(), classParameters[classIndex]);
                }

                // Add

                return this;
            }

            @Override
            public Func getFunc()
            {
                return null;
            }
        }.init(compiledGXScript);
    }
}
