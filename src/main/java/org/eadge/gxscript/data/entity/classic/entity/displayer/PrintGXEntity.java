package org.eadge.gxscript.data.entity.classic.entity.displayer;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

/**
 * Created by eadgyo on 02/08/16.
 *
 */
public class PrintGXEntity extends DefaultGXEntity
{
    public final static int SOURCE_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int CONTINUE_OUTPUT_INDEX = 0;

    private String defaultString = "";

    public PrintGXEntity(String defaultString)
    {
        this();

        this.defaultString = defaultString;
    }

    public PrintGXEntity()
    {
        super("Print");

        // Add source script
        addInputEntryNotNeeded(SOURCE_INPUT_INDEX, "Source", Object.class, defaultString);

        // Add next script
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add continue output
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        // If there is one GXEntity setting printed text
        if (getInputEntity(SOURCE_INPUT_INDEX) != null)
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    // Get the source
                    Object[] objects = program.loadCurrentParametersObjects();
                    Object   source  = objects[SOURCE_INPUT_INDEX];

                    // Print the source
                    System.out.println(source);
                }
            };
        }
        else // No script setting printed text
        {
            // Use default string
            return new Func()
            {
                private String text;

                public Func init(String text)
                {
                    this.text = text;
                    return this;
                }

                @Override
                public void run(Program program)
                {
                    // Print default string
                    System.out.println(text);
                }
            }.init(defaultString);
        }
    }

    public String getDefaultString()
    {
        return defaultString;
    }

    public void setDefaultString(String defaultString)
    {
        this.defaultString = defaultString;
    }
}
