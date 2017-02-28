package org.eadge.gxscript.classic.entity.types.bool;

import org.eadge.gxscript.data.entity.GXEntity;
import org.eadge.gxscript.data.entity.ModifyingGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

/**
 * Created by eadgyo on 04/09/16.
 *
 * Modify bool GXEntity
 */
public class ModifyBoolGXEntity extends ModifyingGXEntity
{
    public final static int MODIFIED_INPUT_INDEX = 0;
    public final static int SOURCE_INPUT_INDEX = 1;
    public final static int NUMBER_INPUT_INDEX = 2;

    public final static int CONTINUE_OUTPUT_INDEX = 0;

    public ModifyBoolGXEntity()
    {
        super("Modify bool");

        addInputEntry(MODIFIED_INPUT_INDEX, "Modified", Boolean.class);
        addInputEntryNotNeeded(SOURCE_INPUT_INDEX, "Set", Boolean.class);
        addInputEntryNotNeeded(NUMBER_INPUT_INDEX, "Number", Number.class);

        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex == MODIFIED_INPUT_INDEX)
        {
            if (isInputUsed(NUMBER_INPUT_INDEX))
            {
                unlinkAsInput(NUMBER_INPUT_INDEX);
            }
        }
        else if (inputIndex == NUMBER_INPUT_INDEX)
        {
            if (isInputUsed(MODIFIED_INPUT_INDEX))
            {
                unlinkAsInput(MODIFIED_INPUT_INDEX);
            }
        }
    }

    @Override
    public Func getFunc()
    {
        if (isInputUsed(MODIFIED_INPUT_INDEX))
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get source to modify
                    Boolean source = (Boolean) objects[SOURCE_INPUT_INDEX];

                    // Get modified address
                    FuncDataAddresses funcParameters = program.getCurrentFuncParameters();
                    DataAddress       address        = funcParameters.getInputAddress(MODIFIED_INPUT_INDEX);

                    // Replace modified value
                    program.setObject(address, source);
                }
            };
        }
        else
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get source to modify
                    Number source = (Number) objects[SOURCE_INPUT_INDEX];

                    // Transform number in source
                    Boolean transformedSource = source.doubleValue() != 0;

                    // Get modified address
                    FuncDataAddresses funcParameters = program.getCurrentFuncParameters();
                    DataAddress       address        = funcParameters.getInputAddress(MODIFIED_INPUT_INDEX);

                    // Replace modified value
                    program.setObject(address, transformedSource);
                }
            };
        }
    }

    @Override
    public boolean hasAllNeededInput()
    {
        return (isInputUsed(MODIFIED_INPUT_INDEX) && (isInputUsed(SOURCE_INPUT_INDEX) || isInputUsed(NUMBER_INPUT_INDEX)));
    }
}
