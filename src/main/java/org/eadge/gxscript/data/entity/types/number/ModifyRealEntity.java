package org.eadge.gxscript.data.entity.types.number;

import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.exception.NumberOverflowException;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Modifying real entity
 */
public class ModifyRealEntity extends ModifyingEntity
{
    public final static int MODIFIED_INPUT_INDEX = 0;
    public final static int SOURCE_INPUT_INDEX = 1;

    public ModifyRealEntity()
    {
        addInputEntry(MODIFIED_INPUT_INDEX, "Modified", Number.class);
        addInputEntry(SOURCE_INPUT_INDEX, "Set", Number.class);
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

                // Get source and convert
                Number modified = (Number) objects[MODIFIED_INPUT_INDEX];
                Number source = (Number) objects[SOURCE_INPUT_INDEX];

                // Get modified address
                FuncDataAddresses currentFuncParameters = program.getCurrentFuncParameters();
                DataAddress       address          = currentFuncParameters.getInputAddress(MODIFIED_INPUT_INDEX);

                // Replace modified value
                Number converted = convert(source, modified.getClass());
                program.setObject(address, converted);
            }

            public Number convert(Number source, Class targetClass)
            {
                if (targetClass.isInstance(Integer.class))
                {
                    long value = source.longValue();
                    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE)
                    {
                        throw new NumberOverflowException();
                    }
                    return source.intValue();
                }
                else if (targetClass.equals(Float.class))
                {
                    return source.floatValue();
                }

                return null;
            }
        };
    }
}
