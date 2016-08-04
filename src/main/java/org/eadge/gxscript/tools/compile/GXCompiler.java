package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.RawGXScript;

import java.util.Map;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Compile code in linear code
 */
public class GXCompiler
{
    /**
     * Hold output index of one entity
     */
    private class OutputIndex
    {
        public int index;
    }

    /**
     * Holds all outputs index for one entity
     */
    private class EntityOutputIndices
    {
        public OutputIndex outputsIndices[];
    }

    private class EntityToOutputIndices
    {
        public Map<Entity, EntityOutputIndices> entityToOutputIndices;
    }


    public CompiledGXScript compile(RawGXScript rawGXScript)
    {

        return null;
    }
}
