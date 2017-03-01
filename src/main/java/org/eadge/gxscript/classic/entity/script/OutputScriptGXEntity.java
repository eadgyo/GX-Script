package org.eadge.gxscript.classic.entity.script;

import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;

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
     * @return created func
     */
    private Func getOutputFunc(OutputAddresses outputAddresses)
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

                // Get the address of the start of the last pushed script
                DataAddress outputAddress = program.getLastPushedLevelAddress().clone();

                // Compute allocated output Address
                outputAddress.addOffset(scriptOutputAddress);

                // Use scriptOutputAddress
                program.setObject(outputAddress, copiedObject);
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
        // Retrieve already allocated address
        OutputAddresses outputAddresses = addressesMap.get(this);

        // Get func and add it to called functions
        Func func = getOutputFunc(outputAddresses);
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
}
