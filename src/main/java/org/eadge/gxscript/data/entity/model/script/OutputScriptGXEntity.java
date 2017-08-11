package org.eadge.gxscript.data.entity.model.script;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.address.OutputAddresses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by eadgyo on 27/02/17.
 *
 * Used to add output for script
 */
public class OutputScriptGXEntity extends DefaultGXEntity
{
    public final static int SCRIPT_OUTPUT_INPUT = 0;

    public OutputScriptGXEntity()
    {
        super("Output");

        // Add continue output
        addInputEntry(SCRIPT_OUTPUT_INPUT, "-", Object.class);
    }

    @Override
    public Func getFunc()
    {
        return null;
    }

    /**
     * Create output func using already allocated script output Addresses
     * @param outputAddresses addresses of allocated output
     * @return created output script objects copy func
     */
    private Func getOutputCopyFunc(OutputAddresses outputAddresses)
    {
        return new Func()
        {
            private DataAddress scriptOutputAddress;

            Func init(OutputAddresses outputAddresses)
            {
                // Get the first output address.
                // Script output address is negative (relative to the start of pushed memory level)
                scriptOutputAddress = outputAddresses.getOutputAddress(0).clone();

                return this;
            }

            @Override
            public void run(Program program)
            {
                // Get object to copy on memory
                Object copiedObject = program.loadCurrentParametersObjects()[SCRIPT_OUTPUT_INPUT];

                // Use scriptOutputAddress
                program.setObject(scriptOutputAddress, copiedObject);
            }
        }.init(outputAddresses);
    }

    @Override
    public void addLinkOutput(int outputIndex, int inputEntityIndex, GXEntity GXEntity)
    {
        super.addLinkOutput(outputIndex, inputEntityIndex, GXEntity);

        updateOutputName(SCRIPT_OUTPUT_INPUT);
    }

    @Override
    public void removeLinkOutput(int outputIndex, GXEntity GXEntity)
    {
        super.removeLinkOutput(outputIndex, GXEntity);

        updateOutputName(SCRIPT_OUTPUT_INPUT);
    }

    /**
     * Set the name of output class
     * @param outputIndex used output index
     */
    private void updateOutputName(int outputIndex)
    {
        Class outputClassFromLinkedEntities = findOutputClassFromLinkedEntities(outputIndex);

        if (outputClassFromLinkedEntities == null)
            setOutputName(outputIndex, "NULL");
        else
            setOutputName(outputIndex, outputClassFromLinkedEntities.getName());
    }

    /**
     * Default add funcs and funcs params
     *
     * @param calledFunctions          list of called script
     * @param calledFunctionAddresses list of used called script data
     * @param addressesMap                      map to link GXEntity to corresponding GXEntity output addresses
     */
    public void pushEntityCode(ArrayList<Func> calledFunctions,
                               ArrayList<FuncDataAddresses> calledFunctionAddresses,
                               Map<GXEntity, OutputAddresses> addressesMap)
    {
    }

    /**
     * Default add funcs and funcs params
     *
     * @param calledFunctions          list of called script
     * @param calledFunctionAddresses list of used called script data
     * @param addressesMap                      map to link GXEntity to corresponding GXEntity output addresses
     */
    public void pushOutputCopyCode(ArrayList<Func> calledFunctions,
                               ArrayList<FuncDataAddresses> calledFunctionAddresses,
                               Map<GXEntity, OutputAddresses> addressesMap)
    {
        // Retrieve already allocated address
        OutputAddresses outputAddresses = addressesMap.get(this);

        // Get func and add it to called functions
        Func func = getOutputCopyFunc(outputAddresses);
        calledFunctions.add(func);

        // Link script to corresponding outputs addresses and add it to called script addresses
        FuncDataAddresses funcDataAddresses = createAndLinkFuncDataAddresses(addressesMap);
        calledFunctionAddresses.add(funcDataAddresses);
    }


    public Collection<Class> getScriptOutputClasses()
    {
        Collection<Class> outputClasses = new ArrayList<>();

        for (int inputIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            outputClasses.add(getOutputClassFromInputEntity(inputIndex));
        }

        return outputClasses;
    }

    /**
     * Create script reserved output addresses
     * @param currentDataAddress current address on stack of data addresses
     * @return reserved output addresses
     */
    public OutputAddresses createScriptOutputAddresses(DataAddress currentDataAddress)
    {
        OutputAddresses outputAddresses = new OutputAddresses();

        // Search for all inputs
        for (int inputIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            if (isVariableInput(inputIndex))
            {
                // Take this address
                outputAddresses.addOutputAddress(inputIndex, currentDataAddress.clone());

                // Alloc a new address
                currentDataAddress.alloc();
            }
        }

        return outputAddresses;
    }

    @Override
    public OutputAddresses createAndAllocOutputs(DataAddress currentDataAddress)
    {
        // OutputAddresses are already allocated, do not alloc output
        return null;
    }

    @Override
    public boolean isScriptOutput()
    {
        return true;
    }

    @Override
    public OutputScriptGXEntity getScriptOutputEntity()
    {
        return this;
    }
}
