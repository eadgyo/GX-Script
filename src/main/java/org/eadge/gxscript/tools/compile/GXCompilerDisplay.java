package org.eadge.gxscript.tools.compile;

import org.eadge.gxscript.data.compile.imbrication.compile.ImbricationNodeCDisplay;
import org.eadge.gxscript.data.compile.script.DisplayCompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.OutputAddresses;
import org.eadge.gxscript.data.entity.model.base.GXEntity;

import java.util.Collection;
import java.util.Map;

public class GXCompilerDisplay extends GXCompiler
{
    @Override
    protected ImbricationNodeCDisplay createImbricationNode(Collection<GXEntity> entities)
    {
        return new ImbricationNodeCDisplay(entities);
    }

    @Override
    protected ImbricationNodeCDisplay createImbricationNode(DataAddress startAddress, Map<GXEntity, OutputAddresses>
            outputAddressesMap, Collection<GXEntity> entities)
    {
        return new ImbricationNodeCDisplay(startAddress, outputAddressesMap, entities);
    }

    @Override
    public DisplayCompiledGXScript compile(RawGXScript rawGXScript)
    {
        return (DisplayCompiledGXScript) super.compile(rawGXScript);
    }
}
