package org.eadge.gxscript.data.entity.model.script;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.script.address.DataAddress;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.address.OutputAddresses;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by eadgyo on 27/02/17.
 *
 * Used to create Input for script
 */
public class InputScriptGXEntity extends DefaultGXEntity
{
    public final static int SCRIPT_INPUT_OUTPUT = 0;

    public InputScriptGXEntity()
    {
        super("Parameter");

        // Add continue output
        addOutputEntry(SCRIPT_INPUT_OUTPUT, "-", Object.class);
    }


    @Override
    public Func getFunc()
    {
        return null;
    }

    @Override
    public void addLinkOutput(int outputIndex, int inputEntityIndex, GXEntity gxEntity)
    {
        super.addLinkOutput(outputIndex, inputEntityIndex, gxEntity);
        updateOutputName(SCRIPT_INPUT_OUTPUT);
    }

    @Override
    public void removeLinkOutput(int outputIndex, GXEntity GXEntity)
    {
        super.removeLinkOutput(outputIndex, GXEntity);

        updateOutputName(SCRIPT_INPUT_OUTPUT);
    }

    /**
     * Set the name of output class
     * @param outputIndex used output index
     */
    private void updateOutputName(int outputIndex)
    {
        Class outputClassFromLinkedEntities = findOutputClassFromLinkedEntities(outputIndex);

        if (outputClassFromLinkedEntities == null)
        {
            setOutputName(outputIndex, "NULL");
            setOutputClass(outputIndex, Object.class);
        }
        else
        {
            setOutputName(outputIndex, outputClassFromLinkedEntities.getSimpleName());
            setOutputClass(outputIndex, outputClassFromLinkedEntities);
        }
    }

    /**
     * Check if this parameter is linked to compatible entries
     * @return true if this parameter entity is linked to compatible entries, false otherwise
     */
    public boolean isValidParameter()
    {
        return findOutputClassFromLinkedEntities(SCRIPT_INPUT_OUTPUT) != null;
    }

    /**
     * Retrieve all script input classes
     *
     * @return Collection of script classes
     */
    public ArrayList<Class> getScriptInputClasses()
    {
        ArrayList<Class> inputClasses = new ArrayList<>();
        inputClasses.add(findOutputClassFromLinkedEntities(SCRIPT_INPUT_OUTPUT));
        return inputClasses;
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
        // Copy is done by the script launcher
    }

    /**
     * Create script reserved input addresses
     * @param currentDataAddress current address on stack of data addresses
     * @return reserved input addresses
     */
    public OutputAddresses createScriptInputAddresses(DataAddress currentDataAddress)
    {
        return super.createAndAllocOutputs(currentDataAddress);
    }

    @Override
    public OutputAddresses createAndAllocOutputs(DataAddress currentDataAddress)
    {
        // OutputAddresses are already allocated, do not alloc output
        return null;
    }

    @Override
    public boolean isScriptInput()
    {
        return true;
    }

    @Override
    public InputScriptGXEntity getScriptInputEntity()
    {
        return this;
    }
}
