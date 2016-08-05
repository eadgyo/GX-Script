package org.eadge.gxscript.data.imbrication.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbricate.ImbricatedEntity;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.imbrication.ImbricationNode;

import java.util.Set;

/**
 * Created by eadgyo on 05/08/16.
 */
public class ImbricationNodeC extends ImbricationNode
{
    private CompiledGXScript compiledGXScript = new CompiledGXScript();

    public ImbricationNodeC(ImbricatedEntity startEntity,
                            Set<Entity> allElements)
    {
        super(startEntity, allElements);
    }

    public ImbricationNodeC(Set<Entity> allElements)
    {
        super(allElements);
    }

    public CompiledGXScript getCompiledGXScript()
    {
        return compiledGXScript;
    }
}
