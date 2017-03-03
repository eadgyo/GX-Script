package org.eadge.gxscript.data.entity.def;

import org.eadge.gxscript.data.entity.base.ModifyingGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Default modifying GXEntity
 */
public class DefaultModifyingGXEntity extends ModifyingGXEntity
{
    public final static int MODIFIED_INPUT_INDEX = 0;
    public final static int SOURCE_INPUT_INDEX = 1;

    public final static int CONTINUE_OUTPUT_INDEX = 0;

    public DefaultModifyingGXEntity(String name, Class sourceClass)
    {
        super("Modify bool");

        addInputEntry(MODIFIED_INPUT_INDEX, "Modified", sourceClass);
        addInputEntryNotNeeded(SOURCE_INPUT_INDEX, "Set", sourceClass);

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

                // Get source to modify
                Object source = objects[SOURCE_INPUT_INDEX];

                // Get modified address
                FuncDataAddresses funcParameters = program.getCurrentFuncParameters();
                DataAddress       address        = funcParameters.getInputAddress(MODIFIED_INPUT_INDEX);

                // Replace modified value
                program.setObject(address, source);
            }
        };
    }
}
