package org.eadge.gxscript.classic.entity.types.number.comparison;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.tools.check.NumberComparator;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Superior than number entity
 */
public class SuperiorThanNumberEntity extends DefaultEntity
{
    public static final int V0_INPUT_INDEX = 0;
    public static final int V1_INPUT_INDEX = 1;

    public final static int RESULT_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    private boolean orEqual = false;

    public SuperiorThanNumberEntity()
    {
        super("Superior than");

        addInputEntry(V0_INPUT_INDEX, "v0", Number.class);

        addInputEntry(V1_INPUT_INDEX, "v1", Number.class);

        addOutputEntry(RESULT_OUTPUT_INDEX, "Result", Boolean.class);

        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    public boolean isOrEqual()
    {
        return orEqual;
    }

    public void setOrEqual(boolean orEqual)
    {
        this.orEqual = orEqual;
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

                @SuppressWarnings("unchecked") int result = numberComparator.compare(v0, v1);

                // Push result in memory
                program.pushInMemory(result == 1 || (orEqual && result == 0));
            }
        };
    }
}
