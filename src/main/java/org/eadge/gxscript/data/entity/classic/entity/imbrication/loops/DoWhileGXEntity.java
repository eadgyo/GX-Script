package org.eadge.gxscript.data.entity.classic.entity.imbrication.loops;

import org.eadge.gxscript.data.entity.model.def.DefaultStartImbricationGXEntity;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.data.compile.script.address.FuncAddress;
import org.eadge.gxscript.data.compile.script.address.FuncImbricationDataAddresses;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Do while GXEntity
 */
public class DoWhileGXEntity extends DefaultStartImbricationGXEntity
{
    public final static int TEST_INPUT_INDEX = 0;
    public final static int NEXT_INPUT_INDEX = 1;

    public final static int DO_OUTPUT_INDEX = 0;
    public final static int CONTINUE_OUTPUT_INDEX = 1;

    public DoWhileGXEntity()
    {
        super("DoWhile");

        // Input
        // Add source script
        addInputEntry(TEST_INPUT_INDEX, "Test", Boolean.class);

        // Add next script
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

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
                Object[] objects;

                // Save start func address of do while
                FuncAddress savedDoWhileFunc = program.getCurrentFuncAddress();

                // Get addresses of imbracted functions
                FuncImbricationDataAddresses funcParameters = program.getCurrentFuncImbricationParameters();

                FuncAddress doAddress = funcParameters.getImbricationAddress(DO_OUTPUT_INDEX);
                FuncAddress continueAddress = funcParameters.getImbricationAddress(CONTINUE_OUTPUT_INDEX);

                do
                {
                    program.runImbrication(doAddress, continueAddress, savedDoWhileFunc);

                    // Update in memory objects
                    objects = program.loadCurrentParametersObjects();

                } while ((Boolean) objects[TEST_INPUT_INDEX]);

                program.setNextFuncAddress(continueAddress);
            }

        };
    }
}
