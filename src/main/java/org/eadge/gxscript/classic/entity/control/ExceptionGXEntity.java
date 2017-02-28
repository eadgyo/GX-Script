package org.eadge.gxscript.classic.entity.control;

import org.eadge.gxscript.data.entity.DefaultStartImbricationGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;

/**
 * Created by eadgyo on 02/08/16.
 *
 */
public class ExceptionGXEntity extends DefaultStartImbricationGXEntity
{
    private Exception exception;

    public static final int NEXT_INPUT_INDEX = 0;

    public static final int TRY_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public ExceptionGXEntity()
    {
        this(new Exception());
    }

    public ExceptionGXEntity(Exception exception)
    {
        super("Exception");

        // Add next index
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Try output index
        addOutputImbricatedEntry(TRY_OUTPUT_INDEX, 0, "Try", Void.class);

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
                // Get addresses of imbricated functions
                FuncImbricationDataAddresses funcParameters = program
                        .getCurrentFuncImbricationParameters();


                FuncAddress tryAddress = funcParameters.getImbricationAddress(TRY_OUTPUT_INDEX);
                FuncAddress continueAddress = funcParameters.getImbricationAddress(CONTINUE_OUTPUT_INDEX);

                program.pushMemoryLevel();

                // Launch program
                try
                {
                    program.runFromAndUntil(tryAddress, continueAddress);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                program.popMemoryLevel();
            }
        };
    }
}
