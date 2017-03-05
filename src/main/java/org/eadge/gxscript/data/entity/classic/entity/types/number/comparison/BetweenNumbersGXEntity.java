package org.eadge.gxscript.data.entity.classic.entity.types.number.comparison;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.tools.check.NumberComparator;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Test if one number is between two numbers entities
 */
public class BetweenNumbersGXEntity extends DefaultGXEntity
{
    public static final int V0_INPUT_INDEX = 0;
    public static final int SOURCE_INPUT_INDEX = 1;
    public static final int V1_INPUT_INDEX = 2;
    public static final int NEXT_INPUT_INDEX = 3;

    public static final int RESULT_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    private boolean v0ComparisonOrEqual = false;
    private boolean v1ComparisonOrEqual = false;

    public BetweenNumbersGXEntity()
    {
        super("Between numbers");

        addInputEntry(V0_INPUT_INDEX, "V0", Number.class);
        addInputEntry(SOURCE_INPUT_INDEX, "Source", Number.class);
        addInputEntry(V1_INPUT_INDEX, "V1", Number.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(RESULT_OUTPUT_INDEX, "Result", Boolean.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    public boolean isV0ComparisonOrEqual()
    {
        return v0ComparisonOrEqual;
    }

    public void setV0ComparisonOrEqual(boolean v0ComparisonOrEqual)
    {
        this.v0ComparisonOrEqual = v0ComparisonOrEqual;
    }

    public boolean isV1ComparisonOrEqual()
    {
        return v1ComparisonOrEqual;
    }

    public void setV1ComparisonOrEqual(boolean v1ComparisonOrEqual)
    {
        this.v1ComparisonOrEqual = v1ComparisonOrEqual;
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            private boolean v0ComparisonOrEqual;
            private boolean v1ComparisonOrEqual;

            public Func init(boolean v0ComparisonOrEqual, boolean v1ComparisonOrEqual)
            {
                this.v0ComparisonOrEqual = v0ComparisonOrEqual;
                this.v1ComparisonOrEqual = v1ComparisonOrEqual;

                return this;
            }

            @Override
            public void run(Program program)
            {
                Object[] objects = program.loadCurrentParametersObjects();

                // Load V0, Source and V1
                Number v0 = (Number) objects[V0_INPUT_INDEX];
                Number source = (Number) objects[SOURCE_INPUT_INDEX];
                Number v1 = (Number) objects[V1_INPUT_INDEX];

                NumberComparator numberComparator = new NumberComparator();

                // Compare
                @SuppressWarnings("unchecked") int res0 = numberComparator.compare(source, v0);
                @SuppressWarnings("unchecked") int res1 = numberComparator.compare(v1, source);

                // Compute result
                boolean fullRes0 = res0 == 1 || (v0ComparisonOrEqual && res0 == 0);
                boolean fullRes1 = res1 == 1 || (v1ComparisonOrEqual && res1 == 0);

                // Push comparison result
                program.pushInMemory(fullRes0 && fullRes1);
            }
        }.init(v0ComparisonOrEqual, v1ComparisonOrEqual);
    }
}
