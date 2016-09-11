package org.eadge.gxscript.classic.entity.types.number.operations.maths;

import org.eadge.gxscript.data.entity.model.OneInputDefinesOneOutput;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Negate number entity
 */
public class NegNumberEntity extends OneInputDefinesOneOutput
{
    public NegNumberEntity()
    {
        super("Negate", Number.class);
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
                Number source = (Number) objects[SOURCE_INPUT_INDEX];

                // Create number and push it
                Number result = negateNumber(source);

                // Push result in memory
                program.pushInMemory(result);
            }

            public Number negateNumber(Number a)
            {
                if (a instanceof Double)
                {
                    return -a.doubleValue();
                }
                else if (a instanceof Float)
                {
                    return -a.floatValue();
                }
                else if (a instanceof Long)
                {
                    return -a.longValue();
                }
                else if (a instanceof Integer)
                {
                    return -a.intValue();
                }
                else
                {
                    return -a.doubleValue();
                }
            }
        };
    }
}