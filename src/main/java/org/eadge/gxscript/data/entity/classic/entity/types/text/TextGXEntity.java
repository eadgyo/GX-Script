package org.eadge.gxscript.data.entity.classic.entity.types.text;

import org.eadge.gxscript.data.entity.model.def.DefaultVariableGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Create text
 */
public class TextGXEntity extends DefaultVariableGXEntity
{
    public final static int SET_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int TEXT_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * String holding default value
     */
    public final static String DEFAULT_STRING = "Text";

    public TextGXEntity(String defaultString)
    {
        super("Create text");

        addInputEntryNotNeeded(SET_INPUT_INDEX, "Set", String.class, defaultString);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(TEXT_OUTPUT_INDEX, "Variable", String.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    public TextGXEntity()
    {
        this(DEFAULT_STRING);
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
            }.init(getDefaultString());
        }
    }

    public void setDefaultString(String defaultString)
    {
        setOptionValue(SET_INPUT_INDEX, defaultString);
    }

    public String getDefaultString()
    {
        return (String) getOptionValue(SET_INPUT_INDEX);
    }
}
