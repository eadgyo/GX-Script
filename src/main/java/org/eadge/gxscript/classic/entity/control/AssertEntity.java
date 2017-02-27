package org.eadge.gxscript.classic.entity.control;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Assertion creator
 */
public class AssertEntity extends DefaultEntity
{
    public final static int SOURCE_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int CONTINUE_OUTPUT_INDEX = 0;

    public AssertEntity()
    {
        super("Assert");

        // Add source checking value
        addInputEntry(SOURCE_INPUT_INDEX, "Checked", Boolean.class);

        // Add next function
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

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
                // Get the source
                Object[] objects = program.loadCurrentParametersObjects();
                Boolean  source  = (Boolean) objects[SOURCE_INPUT_INDEX];

                assert (source);
            }
        };
    }
}
