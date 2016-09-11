package org.eadge.gxscript.classic.entity.types.number.operations.maths;

import org.eadge.gxscript.classic.entity.types.number.operations.maths.model.MathTwoInputsModel;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Divide two numbers
 */
public class MultiplyNumbersEntity extends MathTwoInputsModel
{
    public MultiplyNumbersEntity()
    {
        super("Multiply");
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

                // Get source and mulitplied
                Number source = (Number) objects[S0_INPUT_INDEX];
                Number mulitplied = (Number) objects[S1_INPUT_INDEX];

                // Create number and push it
                Number result = multiplyNumbers(source, mulitplied);

                // Push result in memory
                program.pushInMemory(result);
            }

            public Number multiplyNumbers(Number a, Number b)
            {
                if (a instanceof Double || b instanceof Double)
                {
                    return a.doubleValue() * b.doubleValue();
                }
                else if (a instanceof Float || b instanceof Float)
                {
                    return a.floatValue() * b.floatValue();
                }
                else if (a instanceof Long || b instanceof Long)
                {
                    return a.longValue() * b.longValue();
                }
                else if (a instanceof Integer || b instanceof Integer)
                {
                    return a.intValue() * b.intValue();
                }
                else
                {
                    return a.doubleValue() * b.doubleValue();
                }
            }
        };
    }
}
