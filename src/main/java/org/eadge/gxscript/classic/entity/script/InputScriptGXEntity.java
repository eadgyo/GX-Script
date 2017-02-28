package org.eadge.gxscript.classic.entity.script;

import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.ArrayList;

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
        return new Func()
        {
            private Class outputClass;

            public Func init(Class outputClass)
            {
                this.outputClass = outputClass;

                return this;
            }

            @Override
            public void run(Program program)
            {
                // Get the source
                Object[] objects = program.loadCurrentParametersObjects();
                Object  source  = (outputClass).cast(objects[SCRIPT_INPUT_OUTPUT]);

                program.pushInMemory(source);
            }
        }.init(getOutputClass(SCRIPT_INPUT_OUTPUT));
    }

    @Override
    public void addLinkOutput(int outputIndex, int inputEntityIndex, GXEntity GXEntity)
    {
        super.addLinkOutput(outputIndex, inputEntityIndex, GXEntity);

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
            setOutputName(outputIndex, "NULL");
        else
            setOutputName(outputIndex, outputClassFromLinkedEntities.getName());
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
}
