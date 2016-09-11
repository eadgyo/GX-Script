package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.imbrication.compile.ImbricationNodeC;
import org.eadge.gxscript.data.imbrication.compile.ImbricationNodeCDebug;
import org.eadge.gxscript.data.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.script.RawGXScript;

import java.util.Collection;

/**
 * Created by eadgyo on 11/09/16.
 *
 * GX compiler in debug mode
 */
public class GXCompilerDebug extends GXCompiler
{
    @Override
    protected ImbricationNodeC createImbricationNode(Collection<Entity> entities)
    {
        return new ImbricationNodeCDebug(entities);
    }

    @Override
    public DebugCompiledGXScript compile(RawGXScript rawGXScript)
    {
        return (DebugCompiledGXScript) super.compile(rawGXScript);
    }
}
