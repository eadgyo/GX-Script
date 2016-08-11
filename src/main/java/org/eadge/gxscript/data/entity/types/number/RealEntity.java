package org.eadge.gxscript.data.entity.types.number;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.func.types.number.RealFunc;
import org.eadge.gxscript.data.script.Func;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Real number creator
 */
public class RealEntity extends DefaultEntity
{
    private float defaultFloat = 0;

    public RealEntity()
    {
        addInputEntry(Void.class);

        addInputEntryNotNeeded(Float.class);

        addOutputEntry(Float.class);
    }

    @Override
    public Func getFunc()
    {
        return new RealFunc(defaultFloat);
    }

    public float getDefaultFloat()
    {
        return defaultFloat;
    }

    public void setDefaultFloat(float defaultFloat)
    {
        this.defaultFloat = defaultFloat;
    }
}
