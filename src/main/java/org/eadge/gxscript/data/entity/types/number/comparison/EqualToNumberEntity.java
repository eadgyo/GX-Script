package org.eadge.gxscript.data.entity.types.number.comparison;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.func.types.number.comparison.EqualToNumberFunc;
import org.eadge.gxscript.data.script.Func;

/**
 * Created by eadgyo on 03/08/16.
 */
public class EqualToNumberEntity extends DefaultEntity
{
    public EqualToNumberEntity()
    {
        super("Equal");

        addInputEntry("v0", Number.class);

        addInputEntry("v1", Number.class);

        addOutputEntry("Result", Boolean.class);

        addOutputEntry("Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        return new EqualToNumberFunc();
    }
}
