package org.eadge.gxscript.classic.entity.types.bool;

import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

/**
 * Created by eadgyo on 04/09/16.
 *
 * Modify bool entity
 */
public class ModifyBoolEntity extends ModifyingEntity
{
    public final static int MODIFIED_INPUT_INDEX = 0;
    public final static int SOURCE_INPUT_INDEX = 1;

    public final static int CONTINUE_OUTPUT_INDEX = 0;

    public ModifyBoolEntity()
    {
        super("Modify bool");

        addInputEntry(MODIFIED_INPUT_INDEX, "Modified", Boolean.class);
        addInputEntry(SOURCE_INPUT_INDEX, "Set", Number.class);

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

                // Get modified boolean and the used source to modify
                Boolean modified = (Boolean) objects[MODIFIED_INPUT_INDEX];
                Number source = (Number) objects[SOURCE_INPUT_INDEX];

                // Transform number in source
                Boolean transformedSource = source.doubleValue() != 0;

                // Get modified address
                FuncDataAddresses funcParameters = program.getCurrentFuncParameters();
                DataAddress address = funcParameters.getInputAddress(MODIFIED_INPUT_INDEX);

                // Replace modified value
                program.setObject(address, transformedSource);
            }
        };
    }
}
