package org.eadge.gxscript.classic.entity.types.bool.transform;

import org.eadge.gxscript.data.entity.def.DefaultGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 04/09/16.
 *
 * Inverse bool GXEntity
 */
public class InvertBoolGXEntity extends DefaultGXEntity
{
    public static final int SOURCE_INPUT_INDEX = 0;
    public static final int NEXT_INPUT_INDEX = 1;

    public static final int INVERTED_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public InvertBoolGXEntity()
    {
        super("Invert bool");

        addInputEntry(SOURCE_INPUT_INDEX, "Source", Boolean.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(INVERTED_OUTPUT_INDEX, "Inverted", Boolean.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            @Override
            public void run(Program program)
            {
                Object[] objects = program.loadCurrentParametersObjects();

                // Get boolean
                Boolean source = (Boolean) objects[SOURCE_INPUT_INDEX];

                // Push inverted boolean
                program.pushInMemory(!source);
            }
        };
    }
}
