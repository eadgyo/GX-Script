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
    public final static int SET_INPUT_INDEX  = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int REAL_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * Float holding default value
     */
    private Float defaultFloat = 0f;

    public RealEntity(float defaultFloat)
    {
        this();

        this.defaultFloat = defaultFloat;
    }

    public RealEntity()
    {
        super("Create Real");

        // Inputs
        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", Number.class);

        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        addOutputEntry(REAL_OUTPUT_INDEX, "Variable", Float.class);

        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
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
        else
        {
            // Use default value
            return new Func()
            {
                private float real;

                public Func init(float real)
                {
                    this.real = real;
                    return this;
                }

                @Override
                public void run(Program program)
                {
                    program.pushInMemory(real);
                }
            }.init(defaultFloat);

        }
    }

    public Float getDefaultFloat()
    {
        return defaultFloat;
    }

    public void setDefaultFloat(Float defaultFloat)
    {
        this.defaultFloat = defaultFloat;
    }

    @Override
    public ModifyingEntity createModificationEntity(int outputIndex)
    {
        return new ModifyNumberEntity();
    }
}
