package org.eadge.gxscript.classic.entity.types.number.operations.maths;

import org.eadge.gxscript.classic.entity.types.number.operations.maths.models.MathTwoInputsModel;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Substract one number by another
 */
public class SubNumbersEntity extends MathTwoInputsModel
{
    public SubNumbersEntity(String operation)
    {
        super("Substract");
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

                // Get source and sub
                Number source = (Number) objects[SOURCE_INPUT_ENTITY];
                Number sub = (Number) objects[ADD_INPUT_ENTITY];

                // Create number and push it
                Number result = substractNumbers(source, sub);

                // Push result in memory
                program.pushInMemory(result);
            }

            public Number substractNumbers(Number a, Number b)
            {
                if (a instanceof Double || b instanceof Double)
                {
                    return a.doubleValue() - b.doubleValue();
                }
                else if (a instanceof Float || b instanceof Float)
                {
                    return a.floatValue() - b.floatValue();
                }
                else if (a instanceof Long || b instanceof Long)
                {
                    return a.longValue() - b.longValue();
                }
                else if (a instanceof Integer || b instanceof Integer)
                {
                    return a.intValue() - b.intValue();
                }
                else
                {
                    return a.doubleValue() - b.doubleValue();
                }
            }
        };
    }
}
