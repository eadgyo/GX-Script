package org.eadge.gxscript.classic.entity.types.bool;

import org.eadge.gxscript.data.entity.DefaultVariableEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 */
public class BoolEntity extends DefaultVariableEntity
{
    public final static int SET_INPUT_INDEX  = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int BOOL_OUTPUT_INDEX      = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * Integer holding default value
     */
    private Boolean defaultBool = false;

    public BoolEntity(Boolean defaultBool)
    {
        this();

        this.defaultBool = defaultBool;
    }

    public BoolEntity()
    {
        super("Create Bool");

        // Inputs
        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", Number.class);

        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        addOutputEntry(BOOL_OUTPUT_INDEX, "Variable", Boolean.class);

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
                    Number inputValue = (Number) (objects[SET_INPUT_INDEX]);

                    // Push in memory this value. Don't need to clone float.
                    program.pushInMemory(inputValue.doubleValue() != 0);
                }
            };
        }
        else
        {
            // Use default value
            return new Func()
            {
                private Boolean bool;

                public Func init(Boolean bool)
                {
                    this.bool = bool;
                    return this;
                }

                @Override
                public void run(Program program)
                {
                    program.pushInMemory(bool);
                }
            }.init(defaultBool);

        }
    }

    public Boolean getDefaultBool()
    {
        return defaultBool;
    }

    public void setDefaultBool(Boolean defaultBool)
    {
        this.defaultBool = defaultBool;
    }

    @Override
    public ModifyingEntity createModificationEntity(int outputIndex)
    {
        return new ModifyBoolEntity();
    }
}
