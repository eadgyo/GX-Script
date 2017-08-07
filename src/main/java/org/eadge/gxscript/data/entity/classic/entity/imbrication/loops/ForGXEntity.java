package org.eadge.gxscript.data.entity.classic.entity.imbrication.loops;

import org.eadge.gxscript.data.entity.model.def.DefaultStartImbricationGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.data.compile.script.address.FuncAddress;
import org.eadge.gxscript.data.compile.script.address.FuncImbricationDataAddresses;

/**
 * Created by eadgyo on 02/08/16.
 *
 * For GXEntity
 */
public class ForGXEntity extends DefaultStartImbricationGXEntity
{
    public static final int START_INPUT_INDEX = 0;
    public static final int ADD_INPUT_INDEX = 1;
    public static final int MAX_INPUT_INDEX = 2;
    public static final int NEXT_INPUT_INDEX = 3;

    public static final int DO_OUTPUT_INDEX        = 0;
    public static final int INDEX_FOR_OUTPUT_INDEX = 1;
    public static final int CONTINUE_OUTPUT_INDEX  = 2;

    private Integer start = 0;
    private Integer add   = 1;
    private Integer max   = 10;

    public ForGXEntity()
    {
        super("For");

        addInputEntryNotNeeded(START_INPUT_INDEX, "Start", Integer.class, start);
        addInputEntryNotNeeded(ADD_INPUT_INDEX, "Add", Integer.class, add);
        addInputEntryNotNeeded(MAX_INPUT_INDEX, "Max", Integer.class, max);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputImbricatedEntry(DO_OUTPUT_INDEX, 0, "Do", Void.class);
        addOutputImbricatedEntry(INDEX_FOR_OUTPUT_INDEX, 0, "Index", Integer.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        final Integer startIndex = (getInputEntity(START_INPUT_INDEX) != null) ? null : this.start;
        Integer       add        = (getInputEntity(ADD_INPUT_INDEX) != null) ? null : this.add;
        final Integer endIndex   = (getInputEntity(MAX_INPUT_INDEX) != null) ? null : this.max;

        return new Func()
        {
            private Integer start;
            private Integer add;
            private Integer max;

            Func init(Integer start, Integer add, Integer max)
            {
                this.start = start;
                this.add = add;
                this.max = max;

                return this;
            }

            private int getStart(Object[] objects, int startIndexObj)
            {
                return (startIndexObj == -1) ? this.start : (int) objects[startIndexObj];
            }

            private int getAdd(Object[] objects, int addIndexObj)
            {
                return (addIndexObj == -1) ? this.add : (int) objects[addIndexObj];
            }

            private int getMax(Object[] objects, int maxIndexObj)
            {
                return (maxIndexObj == -1) ? this.max : (int) objects[maxIndexObj];
            }

            @Override
            public void run(Program program)
            {
                Object[] objects = program.loadCurrentParametersObjects();

                int objectIndex = START_INPUT_INDEX;

                // Get indices of source, or -1 if we need to use default value
                int startIndexObj = (start == null) ? objectIndex++ : -1;
                int addIndexObj = (add == null) ? objectIndex++ : -1;
                int maxIndexObj = (max == null) ? objectIndex : -1;

                FuncImbricationDataAddresses parameters = program
                        .getCurrentFuncImbricationParameters();

                FuncAddress doStart = parameters.getImbricationAddress(0);
                FuncAddress doEnd = parameters.getImbricationAddress(1);

                // Start for loop
                for (int index = getStart(objects, startIndexObj);
                        index < getMax(objects, maxIndexObj);
                        index += getAdd(objects, addIndexObj))
                {
                    // Save state of memory
                    program.saveMemoryState();

                    // Push in memory index
                    program.pushInMemory(index);

                    // Call functions
                    program.runFromAndUntil(doStart, doEnd);

                    // Restore state of memory
                    program.restoreMemoryState();
                }

                // Don't need to move the current read address to the continue address, it's already on
            }

        }.init(startIndex, add, endIndex);
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getAdd()
    {
        return add;
    }

    public void setAdd(int add)
    {
        this.add = add;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(int max)
    {
        this.max = max;
    }
}
