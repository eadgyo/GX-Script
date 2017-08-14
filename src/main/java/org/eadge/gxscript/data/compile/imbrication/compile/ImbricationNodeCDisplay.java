package org.eadge.gxscript.data.compile.imbrication.compile;

import org.eadge.gxscript.data.compile.script.DisplayCompiledGXScript;
import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.address.OutputAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.base.StartImbricationGXEntity;
import org.eadge.gxscript.data.entity.model.script.OutputScriptGXEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ImbricationNodeCDisplay extends ImbricationNodeC
{
    /**
     * Called func
     */
    protected ArrayList<String> calledFunctionsName = new ArrayList<>();

    public ImbricationNodeCDisplay(int imbricationNode,
                                   StartImbricationGXEntity startImbricationEntity,
                                   Set<GXEntity> allElements)
    {
        super(imbricationNode, startImbricationEntity, allElements);
    }

    public ImbricationNodeCDisplay(DataAddress startAddress,
                                   Map<GXEntity, OutputAddresses> outputAddressesMap,
                                   Collection<GXEntity> allElements)
    {
        super(startAddress, outputAddressesMap, allElements);
    }

    public ImbricationNodeCDisplay(Collection<GXEntity> allElements)
    {
        super(allElements);
    }

    public ImbricationNodeCDisplay(DataAddress dataAddress,
                                   Collection<Func> GXScriptFuncParameters,
                                   Collection<FuncDataAddresses> GXScriptFuncParametersAddresses,
                                   Collection<GXEntity> allElements)
    {
        super(dataAddress, GXScriptFuncParameters, GXScriptFuncParametersAddresses, allElements);
    }

    public ImbricationNodeCDisplay(Set<GXEntity> allElements)
    {
        super(allElements);
    }

    public ArrayList<String> getCalledFunctionsName()
    {
        return calledFunctionsName;
    }

    /**
     * Push code of this imbrication node at this level
     *
     * @param imbricationNodeC used imbrication node
     */
    protected void updateAndPushCode(ImbricationNodeC imbricationNodeC)
    {
        super.updateAndPushCode(imbricationNodeC);

        ImbricationNodeCDisplay imbricationNodeCDisplay = (ImbricationNodeCDisplay) imbricationNodeC;

        // Save output addresses
        calledFunctionsName.addAll(imbricationNodeCDisplay.getCalledFunctionsName());
    }

    @Override
    public ImbricationNodeC createImbrication(int imbricationIndex,
                      StartImbricationGXEntity startImbricationEntity,
                      Set<GXEntity> inImbricationEntities)
    {
        return new ImbricationNodeCDisplay(imbricationIndex, startImbricationEntity, inImbricationEntities);
    }

    @Override
    public void treatEntity(GXEntity gxEntity)
    {
        super.treatEntity(gxEntity);
        gxEntity.treatNameAdding(calledFunctionsName);
    }

    @Override
    public void treatOutputEntity(OutputScriptGXEntity outputEntity)
    {
        super.treatOutputEntity(outputEntity);
        outputEntity.treatNameAdding(calledFunctionsName);
    }

    @Override
    public void treatStartImbricationEntity(StartImbricationGXEntity startImbricationEntity)
    {
        super.treatStartImbricationEntity(startImbricationEntity);
        startImbricationEntity.treatNameAdding(calledFunctionsName);
    }

    public DisplayCompiledGXScript compile()
    {
        return new DisplayCompiledGXScript(new ArrayList<Class>(), new ArrayList<Class>(), new ArrayList<String>(), new
                ArrayList<String>(),calledFunctions, calledFunctionsParameters, calledFunctionsName);
    }

    public DisplayCompiledGXScript compile(Collection<Class> inputsScriptClasses,
                                           Collection<Class> outputsScriptClasses,
                                           Collection<String> inputsScriptNames,
                                           Collection<String> outputsScriptNames)
    {
        return new DisplayCompiledGXScript(inputsScriptClasses, outputsScriptClasses, inputsScriptNames,
                                           outputsScriptNames,
                                           calledFunctions,
                                           calledFunctionsParameters, calledFunctionsName);
    }
}
