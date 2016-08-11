package org.eadge.gxscript.data.entity.types.number;

import org.eadge.gxscript.data.entity.DefaultVariableEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Real number creator
 */
public class RealEntity extends DefaultVariableEntity
{
    private final static int SET_INPUT_INDEX  = 0;

    /**
     * Float holding default value
     */
    private float defaultFloat = 0;

    public RealEntity()
    {
        super("Create Real");

        // Inputs
        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", Float.class);

        addInputEntry("", Void.class);

        // Output
        addOutputEntry("Variable", Float.class);
    }

    @Override
    public Func getFunc()
    {
        // If there value is defined from another entity
        if (getInputEntity(SET_INPUT_INDEX) != null)
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    // Load parameters
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get value
                    float inputValue = (float) (objects[SET_INPUT_INDEX]);

                    // Push in memory this value. Don't need to clone float.
                    program.pushInMemory(inputValue);
                }
            };
        }
        else // Else value is a default value
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    program.pushInMemory(defaultFloat);
                }
            };

        }
    }

    public float getDefaultFloat()
    {
        return defaultFloat;
    }

    public void setDefaultFloat(float defaultFloat)
    {
        this.defaultFloat = defaultFloat;
    }

    @Override
    public ModifyingEntity createModificationEntity(int outputIndex)
    {
        return new ModifyRealEntity();
    }
}
