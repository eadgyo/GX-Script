package org.eadge.gxscript.data.entity.classic.entity.types.number.comparison;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.tools.check.NumberComparator;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Equal to number GXEntity
 */
public class EqualToNumberGXEntity extends DefaultGXEntity
{
    public static final int V0_INPUT_INDEX = 0;
    public static final int V1_INPUT_INDEX = 1;
    public static final int NEXT_INPUT_INDEX = 2;

    public final static int RESULT_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    public EqualToNumberGXEntity()
    {
        super("Equal");

        addInputEntry(V0_INPUT_INDEX, "v0", Number.class);
        addInputEntry(V1_INPUT_INDEX, "v1", Number.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(RESULT_OUTPUT_INDEX, "Result", Boolean.class);
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
                // Get variables
                Object[] objects = program.loadCurrentParametersObjects();

                Number v0 = (Number) objects[V0_INPUT_INDEX];
                Number v1 = (Number) objects[V1_INPUT_INDEX];

                NumberComparator numberComparator = new NumberComparator();

                @SuppressWarnings("unchecked") Boolean areEquals = numberComparator.compare(v0, v1) == 0;

                program.pushInMemory(areEquals);
            }
        }; 
    }
}
