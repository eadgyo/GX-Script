package org.eadge.gxscript.data.compile.imbrication.compile;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.script.OutputScriptGXEntity;
import org.eadge.gxscript.data.entity.model.base.StartImbricationGXEntity;
import org.eadge.gxscript.data.compile.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.DebugMemory;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Imbrication node used for debug
 */
public class ImbricationNodeCDebug extends ImbricationNodeC
{
    /**
     * Memory debug information
     */
    private ArrayList<DebugMemory> debugMemoriesAddresses = new ArrayList<>();


    public ImbricationNodeCDebug(int imbricationNode,
                                 StartImbricationGXEntity startImbricationEntity,
                                 Set<GXEntity> allElements)
    {
        super(imbricationNode, startImbricationEntity, allElements);
    }

    public ImbricationNodeCDebug(Collection<GXEntity> allElements)
    {
        super(allElements);
    }

    public ImbricationNodeCDebug(DataAddress dataAddress,
                                 Collection<Func> GXScriptFuncParameters,
                                 Collection<FuncDataAddresses> GXScriptFuncParametersAddresses,
                                 Collection<GXEntity> allElements,
                                 ArrayList<DebugMemory> debugMemoriesAddresses)
    {
        super(dataAddress, GXScriptFuncParameters, GXScriptFuncParametersAddresses, allElements);
        this.debugMemoriesAddresses = debugMemoriesAddresses;
    }

    public ImbricationNodeCDebug(Set<GXEntity> allElements)
    {
        super(allElements);
    }

    @Override
    protected ImbricationNodeCDebug createImbrication(int imbricationIndex,
                                                 StartImbricationGXEntity startImbricationEntity,
                                                 Set<GXEntity> inImbricationEntities)
    {
        return new ImbricationNodeCDebug(imbricationIndex, startImbricationEntity, inImbricationEntities);
    }

    public ArrayList<DebugMemory> getDebugMemoriesAddresses()
    {
        return debugMemoriesAddresses;
    }

    /**
     * Push code of this imbrication node at this level
     *
     * @param imbricationNodeC used imbrication node
     */
    protected void updateAndPushCode(ImbricationNodeC imbricationNodeC)
    {
        super.updateAndPushCode(imbricationNodeC);

        ImbricationNodeCDebug imbricationNodeCDebug = (ImbricationNodeCDebug) imbricationNodeC;

        ArrayList list = new ArrayList();

        // Update memory addresses of this imbrication node
        updateDebugMemoryAddresses(imbricationNodeCDebug.getDebugMemoriesAddresses());

        // Save output addresses
        debugMemoriesAddresses.addAll(imbricationNodeCDebug.getDebugMemoriesAddresses());
    }

    /**
     * Update addresses location of data with this level of imbrication
     *
     * @param debugMemories updated data addresses
     */
    private void updateDebugMemoryAddresses(Collection<DebugMemory> debugMemories)
    {
        for (DebugMemory debugMemory : debugMemories)
        {
            debugMemory.selfAddOffset(currentDataAddress);
        }
    }

    public void treatEntity(GXEntity gxEntity)
    {
        super.treatEntity(gxEntity);

        debugMemoriesAddresses.add(new DebugMemory(currentDataAddress));
    }

    public void treatOutputEntity(OutputScriptGXEntity outputEntity)
    {
        super.treatOutputEntity(outputEntity);

        debugMemoriesAddresses.add(new DebugMemory(currentDataAddress));
    }

    @Override
    public void treatStartImbricationEntity(StartImbricationGXEntity startImbricationEntity)
    {
        super.treatStartImbricationEntity(startImbricationEntity);

        debugMemoriesAddresses.add(new DebugMemory(currentDataAddress));
    }

    public DebugCompiledGXScript compile()
    {
        return new DebugCompiledGXScript(new ArrayList<Class>(), new ArrayList<Class>(), calledFunctions, calledFunctionsParameters, debugMemoriesAddresses);
    }

    public DebugCompiledGXScript compile(Collection<Class> inputsScriptClasses, Collection<Class> outputsScriptClasses)
    {
        return new DebugCompiledGXScript(inputsScriptClasses, outputsScriptClasses, calledFunctions, calledFunctionsParameters, debugMemoriesAddresses);
    }
}
