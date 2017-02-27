package org.eadge.gxscript.data.imbrication.compile;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.StartImbricationEntity;
import org.eadge.gxscript.data.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.DebugMemory;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

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
                                 StartImbricationEntity startImbricationEntity,
                                 Set<Entity> allElements)
    {
        super(imbricationNode, startImbricationEntity, allElements);
    }

    public ImbricationNodeCDebug(Collection<Entity> allElements)
    {
        super(allElements);
    }

    public ImbricationNodeCDebug(DataAddress dataAddress,
                                 Collection<Func> GXScriptFuncParameters,
                                 Collection<FuncDataAddresses> GXScriptFuncParametersAddresses,
                                 Collection<Entity> allElements,
                                 ArrayList<DebugMemory> debugMemoriesAddresses)
    {
        super(dataAddress, GXScriptFuncParameters, GXScriptFuncParametersAddresses, allElements);
        this.debugMemoriesAddresses = debugMemoriesAddresses;
    }

    public ImbricationNodeCDebug(Set<Entity> allElements)
    {
        super(allElements);
    }

    @Override
    protected ImbricationNodeCDebug createImbrication(int imbricationIndex,
                                                 StartImbricationEntity startImbricationEntity,
                                                 Set<Entity> inImbricationEntities)
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
            debugMemory.addOffset(currentDataAddress);
        }
    }

    public void treatEntity(Entity entity)
    {
        super.treatEntity(entity);

        debugMemoriesAddresses.add(new DebugMemory(currentDataAddress));
    }

    @Override
    public void treatStartImbricationEntity(StartImbricationEntity startImbricationEntity)
    {
        super.treatStartImbricationEntity(startImbricationEntity);

        debugMemoriesAddresses.add(new DebugMemory(currentDataAddress));
    }

    public DebugCompiledGXScript compile()
    {
        return new DebugCompiledGXScript(calledFunctions, calledFunctionsParameters, debugMemoriesAddresses);
    }
}
