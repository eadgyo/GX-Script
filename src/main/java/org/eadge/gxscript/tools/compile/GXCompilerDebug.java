package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.compile.imbrication.compile.ImbricationNodeC;
import org.eadge.gxscript.data.compile.imbrication.compile.ImbricationNodeCDebug;
import org.eadge.gxscript.data.compile.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;

import java.util.Collection;

/**
 * Created by eadgyo on 11/09/16.
 *
 * GX compiler in debug mode
 */
public class GXCompilerDebug extends GXCompiler
{
    @Override
    protected ImbricationNodeC createImbricationNode(Collection<GXEntity> entities)
    {
        return new ImbricationNodeCDebug(entities);
    }

    @Override
    public DebugCompiledGXScript compile(RawGXScript rawGXScript)
    {
        return (DebugCompiledGXScript) super.compile(rawGXScript);
    }
}
