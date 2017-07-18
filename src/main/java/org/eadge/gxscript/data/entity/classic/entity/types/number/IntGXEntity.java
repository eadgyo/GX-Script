package org.eadge.gxscript.data.entity.classic.entity.types.number;

import org.eadge.gxscript.data.entity.model.def.DefaultVariableGXEntity;
import org.eadge.gxscript.data.entity.model.base.ModifyingGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Integer GXEntity
 */
public class IntGXEntity extends DefaultVariableGXEntity
{
    public final static int SET_INPUT_INDEX  = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int INT_OUTPUT_INDEX      = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * Integer holding default value
     */
    private Integer defaultInt = 0;

    public IntGXEntity(int defaultInt)
    {
        this();

        this.defaultInt = defaultInt;
    }

    public IntGXEntity()
    {
        super("Create Int");

        // Inputs
        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", Number.class);

        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        addOutputEntry(INT_OUTPUT_INDEX, "Variable", Integer.class);

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
                    program.pushInMemory(inputValue.intValue());
                }
            };
        }
        else
        {
            // Use default value
            return new Func()
            {
                private int integer;

                public Func init(int integer)
                {
                    this.integer = integer;
                    return this;
                }

                @Override
                public void run(Program program)
                {
                    program.pushInMemory(integer);
                }
            }.init(defaultInt);

        }
    }

    public Integer getDefaultInt()
    {
        return defaultInt;
    }

    public void setDefaultInt(Integer defaultInt)
    {
        this.defaultInt = defaultInt;
    }

    @Override
    public ModifyingGXEntity createModificationEntity(int outputIndex)
    {
        return new ModifyNumberGXEntity();
    }
}
