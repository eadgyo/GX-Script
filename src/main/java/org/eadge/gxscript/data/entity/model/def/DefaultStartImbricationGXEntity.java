package org.eadge.gxscript.data.entity.model.def;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.base.StartImbricationGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.script.address.*;

import java.util.*;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Default GXEntity to create imbricated outputs
 */
public abstract class DefaultStartImbricationGXEntity extends DefaultGXEntity implements StartImbricationGXEntity
{
    /**
     * Holds imbrication of outputs, -1 for not imbricated outputs
     */
    protected ArrayList<Integer> imbricationOfOutputs = new ArrayList<>();

    public DefaultStartImbricationGXEntity(String name)
    {
        super(name);
    }

    public DefaultStartImbricationGXEntity()
    {
    }

    @Override
    public int getNumberOfParallelsImbrications()
    {
        int max = -1;

        // Search for the highest imbrication level
        for (Integer imbricationOfOutput : imbricationOfOutputs)
        {
            if (imbricationOfOutput > max)
            {
                max = imbricationOfOutput;
            }
        }
        return max + 1;
    }

    @Override
    public int getOutputImbrication(int index)
    {
        return imbricationOfOutputs.get(index);
    }

    @Override
    public boolean isInImbrication(int index)
    {
        return imbricationOfOutputs.get(index) != -1;
    }

    @Override
    public Collection<GXEntity> getImbricatedOutputs(int level)
    {
        Set<GXEntity> imbricatedOutputs = new HashSet<>();

        // Search for all imbricated outputs at this level
        for (int outputIndex = 0; outputIndex < imbricationOfOutputs.size(); outputIndex++)
        {
            Integer imbricationOfOutput = imbricationOfOutputs.get(outputIndex);

            if (imbricationOfOutput == level)
            {
                imbricatedOutputs.addAll(getOutputEntities(outputIndex));
            }
        }

        return imbricatedOutputs;
    }

    @Override
    public Collection<GXEntity> getNotImbricatedOutputs()
    {
        return getImbricatedOutputs(-1);
    }

    /**
     * Create an imbricated output entry at the given index
     *
     * @param imbricatedIndex imbrication of the output, -1 for no imbrication
     * @param outputName      name of output entry
     * @param cl              class of output
     */
    public void addOutputImbricatedEntry(int imbricatedIndex, String outputName, Class cl)
    {
        addOutputImbricatedEntry(getNumberOfParallelsImbrications(), imbricatedIndex, outputName, cl);
    }

    /**
     * Create an imbricated output entry at the given index
     *
     * @param outputIndex     output index
     * @param imbricatedIndex imbrication of the output, -1 for no imbrication
     * @param outputName      name of output entry
     * @param cl              class of output
     */
    protected void addOutputImbricatedEntry(int outputIndex, int imbricatedIndex, String outputName, Class cl)
    {
        super.addOutputEntry(outputIndex, outputName, cl);

        imbricationOfOutputs.add(outputIndex, imbricatedIndex);
    }

    /**
     * Change output imbricated index
     *
     * @param outputIndex output index
     * @param imbricatedIndex new imbricated index
     */
    public void setOutputImbricatedIndex(int outputIndex, int imbricatedIndex)
    {
        imbricationOfOutputs.set(outputIndex, imbricatedIndex);
    }

    @Override
    public void addOutputEntry(int outputIndex, String outputName, Class cl)
    {
        addOutputImbricatedEntry(outputIndex, -1, outputName, cl);
    }

    @Override
    public void removeOutputEntry(int outputIndex)
    {
        super.removeOutputEntry(outputIndex);

        imbricationOfOutputs.remove(outputIndex);
    }

    /**
     * Create collection of addresses containing parameters addresses of script
     * For n imbrication, they are n + 1 addresses, the first n addresses are for start of imbrication and the last
     * address for the end of last imbrication
     *
     * @param addressesMap map to get script memory stack
     *
     * @return created parameters addresses of script
     */
    protected FuncImbricationDataAddresses createFuncDataAddresses(Map<GXEntity, OutputAddresses> addressesMap,
                                                                   FuncAddress[] imbricatedStartFuncAddresses)
    {
        FuncImbricationDataAddresses funcDataAddresses = new FuncImbricationDataAddresses(getNumberOfUsedInputs(),
                                                                                          getNumberOfParallelsImbrications());

        initFuncDataAddresses(addressesMap, funcDataAddresses);
        funcDataAddresses.setImbricationsAddresses(imbricatedStartFuncAddresses);

        return funcDataAddresses;
    }

    @Override
    public OutputAddresses createAndAllocImbricatedOutputs(int imbricationOutputIndex, DataAddress currentDataAddress)
    {
        OutputAddresses outputAddresses = new OutputAddresses();

        // For all imbricated levels
        for (int outputIndex = 0; outputIndex < getNumberOfOutputs(); outputIndex++)
        {
            // If the level of imbrication is the same
            if (getOutputImbrication(outputIndex) == imbricationOutputIndex)
            {
                // If the output is an output variable
                if (isVariableOutput(outputIndex))
                {
                    // Save address of output
                    outputAddresses.addOutputAddress(outputIndex, currentDataAddress.clone());

                    // Alloc a new address
                    currentDataAddress.alloc();
                }
            }
        }

        return outputAddresses;
    }

    @Override
    public void pushStartImbricationCode(Map<GXEntity, OutputAddresses> addressesMap,
                                         FuncAddress[] imbricatedStartFuncAddresses,
                                         ArrayList<Func> calledFunctions,
                                         ArrayList<FuncDataAddresses> calledFunctionsParameters)
    {
        // Add the func
        calledFunctions.add(getFunc());

        // Create script parameters addresses
        FuncImbricationDataAddresses funcParameters = createFuncDataAddresses(addressesMap,
                                                                              imbricatedStartFuncAddresses);

        // Add link parameters to calledFunctionParameters
        calledFunctionsParameters.add(funcParameters);
    }

    @Override
    public boolean isImbrication()
    {
        return true;
    }

    @Override
    public StartImbricationGXEntity getStartImbricationEntity()
    {
        return this;
    }

    @Override
    public void treatNameAdding(List<String> names)
    {
        names.add(0, name);
    }
}
