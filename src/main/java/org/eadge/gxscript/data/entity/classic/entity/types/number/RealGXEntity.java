package org.eadge.gxscript.data.entity.classic.entity.types.number;

import org.eadge.gxscript.data.entity.model.def.DefaultVariableGXEntity;
import org.eadge.gxscript.data.entity.model.base.ModifyingGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Real number creator
 */
public class RealGXEntity extends DefaultVariableGXEntity
{
    public final static int SET_INPUT_INDEX  = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int REAL_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * Float holding default value
     */
    private Float defaultFloat = 0f;

    public RealGXEntity(float defaultFloat)
    {
        this();

        this.defaultFloat = defaultFloat;
    }

    public RealGXEntity()
    {
        super("Create Real");

        // Inputs
        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", Number.class, defaultFloat);

        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        addOutputEntry(REAL_OUTPUT_INDEX, "Variable", Float.class);

        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        // If there value is defined from another GXEntity
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
                    Number inputValue = (Number) (objects[SET_INPUT_INDEX]);

                    // Push in memory this value. Don't need to clone float.
                    program.pushInMemory(inputValue.floatValue());
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
    public ModifyingGXEntity createModificationEntity(int outputIndex)
    {
        return new ModifyNumberGXEntity();
    }

    @Override
    public Class getVariableClass()
    {
        return Float.class;
    }
}
