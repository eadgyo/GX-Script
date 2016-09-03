package org.eadge.gxscript.classic.entity.imbrication.loops;

import org.eadge.gxscript.data.entity.DefaultStartImbricationEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Do while entity
 */
public class DoWhileEntity extends DefaultStartImbricationEntity
{
    public final static int TEST_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int DO_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    public DoWhileEntity()
    {
        super("DoWhile");

        // Input
        // Add source input
        addInputEntry(TEST_INPUT_INDEX, "Test", Boolean.class);

        // Add next input
        addInputEntry(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        // Do
        addOutputImbricatedEntry(DO_OUTPUT_INDEX, 0, "Do", void.class);

        // Continue
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

                // Get addresses of imbracted functions
                FuncImbricationDataAddresses funcParameters = program.getCurrentFuncImbricationParameters();

                FuncAddress doAddress = funcParameters.getImbricationAddress(DO_OUTPUT_INDEX);
                FuncAddress continueAddress = funcParameters.getImbricationAddress(CONTINUE_OUTPUT_INDEX);

                do
                {
                    // Save the current state of memory
                    program.pushMemoryLevel();

                    // Call functions
                    program.runFromAndUntil(doAddress, continueAddress);

                    // Remove added memory
                    program.popMemoryLevel();

                    // Update in memory objects
                    objects = program.loadCurrentParametersObjects();

                } while ((Boolean) objects[TEST_INPUT_INDEX]);

                program.setNextFuncAddress(continueAddress);
            }

        };
    }
}
