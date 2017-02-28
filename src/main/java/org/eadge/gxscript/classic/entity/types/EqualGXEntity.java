package org.eadge.gxscript.classic.entity.types;

import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Equal objects comparison GXEntity
 */
public class EqualGXEntity extends DefaultGXEntity
{
    public static final int V0_INPUT_INDEX = 0;
    public static final int V1_INPUT_INDEX = 1;

    public static final int NEXT_INPUT_INDEX = 2;

    public static final int RESULT_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public EqualGXEntity()
    {
        super("Equal");

        // Add object 0 function
        addInputEntry(V0_INPUT_INDEX, "V0", Boolean.class);

        // Add object 1 function
        addInputEntry(V1_INPUT_INDEX, "V1", Boolean.class);

        // Add next function
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add output entry
        addOutputEntry(RESULT_OUTPUT_INDEX, "Result", Boolean.class);

        // Add continue output
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

                // Get bool 0 and bool 1
                Object v0 = objects[V0_INPUT_INDEX];
                Object v1= (Boolean) objects[V1_INPUT_INDEX];

                // Push test result
                program.pushInMemory(v0.equals(v1));
            }
        };
    }
}
