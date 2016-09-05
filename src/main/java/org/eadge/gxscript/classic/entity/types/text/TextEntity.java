package org.eadge.gxscript.classic.entity.types.text;

import org.eadge.gxscript.data.entity.DefaultVariableEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Create text
 */
public class TextEntity extends DefaultVariableEntity
{
    public final static int SET_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int TEXT_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * String holding default value
     */
    private String defaultString = "Text";

    public TextEntity()
    {
        super("Create text");

        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", String.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(TEXT_OUTPUT_INDEX, "Variable", String.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    public TextEntity(String defaultString)
    {
        this();

        this.defaultString = defaultString;
    }

    @Override
    public ModifyingEntity createModificationEntity(int outputIndex)
    {
        return null;
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
                    String inputValue = (String) (objects[SET_INPUT_INDEX]);

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
                private String string;

                public Func init(String string)
                {
                    this.string = string;
                    return this;
                }

                @Override
                public void run(Program program)
                {
                    program.pushInMemory(string);
                }
            }.init(defaultString);
        }
    }
}