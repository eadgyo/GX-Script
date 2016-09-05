package org.eadge.gxscript.classic.entity.types.number.operations.maths.models;

import org.eadge.gxscript.data.entity.special.TwoInputsDefineOneOutput;

/**
 * Created by eadgyo on 04/09/16.
 *
 * Math operation model
 */
public abstract class MathTwoInputsModel extends TwoInputsDefineOneOutput
{
    public MathTwoInputsModel(String operation)
    {
        super(operation, "Source", operation, Number.class);
    }

    @Override
    public Class getOutputClass(Class s0OutputClass, Class s1OutputClass)
    {
        if (s0OutputClass == Double.class || s1OutputClass == Double.class)
        {
            return Double.class;
        }
        else if (s0OutputClass == Float.class || s1OutputClass == Float.class)
        {
            return Float.class;
        }
        else if (s0OutputClass == Long.class || s1OutputClass == Long.class)
        {
            return Long.class;
        }
        else if (s0OutputClass == Integer.class || s1OutputClass == Integer.class)
        {
            return Integer.class;
        }
        else
        {
            return Number.class;
        }
    }
}
