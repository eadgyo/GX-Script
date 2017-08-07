package org.eadge.gxscript.data.entity.classic.entity.types.bool;

import org.eadge.gxscript.data.entity.model.def.DefaultVariableGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.base.ModifyingGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 */
public class BoolGXEntity extends DefaultVariableGXEntity
{
    public final static int SET_INPUT_INDEX  = 0;
    public final static int NUMBER_INPUT_INDEX = 1;
    public final static int NEXT_INPUT_INDEX = 2;

    public final static int BOOL_OUTPUT_INDEX      = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * Integer holding default value
     */
    public final static Boolean DEFAULT_BOOL = false;

    public BoolGXEntity()
    {
        this(DEFAULT_BOOL);
    }

    public BoolGXEntity(boolean defaultBool)
    {
        super("Create Bool");

        // Inputs
        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", Boolean.class, defaultBool);
        addInputEntryNotNeeded(NUMBER_INPUT_INDEX, "Number", Number.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        addOutputEntry(BOOL_OUTPUT_INDEX, "Variable", Boolean.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex == SET_INPUT_INDEX)
        {
            if (isInputUsed(NUMBER_INPUT_INDEX))
            {
                unlinkAsInput(NUMBER_INPUT_INDEX);
            }
        }
        else if (inputIndex == NUMBER_INPUT_INDEX)
        {
            if (isInputUsed(SET_INPUT_INDEX))
            {
                unlinkAsInput(SET_INPUT_INDEX);
            }
        }
    }

    @Override
    public Func getFunc()
    {
        // If there value is defined from another GXEntity
        if (isInputUsed(SET_INPUT_INDEX))
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    // Load parameters
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get value
                    Boolean inputValue = (Boolean) (objects[SET_INPUT_INDEX]);

                    // Push in memory this value.
                    program.pushInMemory(inputValue);
                }
            };
        }
        else if (isInputUsed(NUMBER_INPUT_INDEX))
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    // Load parameters
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get value
                    Number inputValue = (Number) (objects[SET_INPUT_INDEX]); // Set script and not NUMBER (there is only one script)

                    // Push in memory this value transformed in boolean.
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
            }.init(getDefaultBool());

        }
    }

    public boolean getDefaultBool()
    {
        return (boolean) getOptionValue(SET_INPUT_INDEX);
    }

    public void setDefaultBool(boolean defaultBool)
    {
        setOptionValue(SET_INPUT_INDEX, defaultBool);
    }

    @Override
    public ModifyingGXEntity createModificationEntity(int outputIndex)
    {
        return new ModifyBoolGXEntity();
    }
}
