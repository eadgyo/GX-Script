package org.eadge.gxscript.data.entity.imbrication.conditionals;

import org.eadge.gxscript.data.entity.DefaultStartImbricationEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;

/**
 * Created by eadgyo on 02/08/16.
 *
 * If entity block
 */
public class IfEntity extends DefaultStartImbricationEntity
{
    public static final int TEST_INPUT_INDEX = 0;

    public static final int SUCCESS_OUTPUT_INDEX = 0;
    public static final int FAIL_OUTPUT_INDEX = 1;
    public static final int CONTINUE_OUTPUT_INDEX = 2;

    public IfEntity()
    {
        super("If");

        addInputEntry(TEST_INPUT_INDEX, "Test", Boolean.class);

        // Success
        addImbricatedOutputEntry(SUCCESS_OUTPUT_INDEX, 0, "Success", Void.class);

        // Fail
        addImbricatedOutputEntry(FAIL_OUTPUT_INDEX, 1, "Fail", Void.class);

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

                // Get test result
                Boolean test = (Boolean) objects[TEST_INPUT_INDEX];

                // Get addresses of imbricated functions
                FuncImbricationDataAddresses funcParameters = program
                        .getCurrentFuncImbricationParameters();

                FuncAddress successAddress = funcParameters.getImbricationAddress(SUCCESS_OUTPUT_INDEX);
                FuncAddress failAddress = funcParameters.getImbricationAddress(FAIL_OUTPUT_INDEX);
                FuncAddress continueAddress = funcParameters.getImbricationAddress(CONTINUE_OUTPUT_INDEX);

                if (test)
                {
                    // Success
                    program.runFromAndUntil(successAddress, failAddress);
                }
                else
                {
                    // Fail
                    program.runFromAndUntil(failAddress, continueAddress);
                }

                program.setCurrentFuncAddress(continueAddress);
            }
        };
    }
}
