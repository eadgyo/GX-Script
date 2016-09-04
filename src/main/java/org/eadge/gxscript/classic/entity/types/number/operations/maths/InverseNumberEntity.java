package org.eadge.gxscript.classic.entity.types.number.operations.maths;

import org.eadge.gxscript.classic.entity.types.number.operations.maths.models.MathOneInputModel;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Inverse number
 */
public class InverseNumberEntity extends MathOneInputModel
{
    public InverseNumberEntity()
    {
        super("Inverse");
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            @Override
            public void run(Program program)
            {
                Object objects[] = program.loadCurrentParametersObjects();

                // Get source
                Number source = (Number) objects[SOURCE_INPUT_ENTITY];

                // Create number and push it
                Number result = inverseNumber(source);

                // Push result in memory
                program.pushInMemory(result);
            }

            public Number inverseNumber(Number a)
            {
                if (a instanceof Double)
                {
                    return 1 / a.doubleValue();
                }
                else if (a instanceof Float)
                {
                    return 1 / a.floatValue();
                }
                else if (a instanceof Long)
                {
                    return 1 / a.longValue();
                }
                else if (a instanceof Integer)
                {
                    return 1 / a.intValue();
                }
                else
                {
                    return 1 / a.doubleValue();
                }
            }
        };
    }
}
