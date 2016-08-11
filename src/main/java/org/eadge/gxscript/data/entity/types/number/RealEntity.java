package org.eadge.gxscript.data.entity.types.number;

import org.eadge.gxscript.data.entity.DefaultVariableEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.func.types.number.RealFunc;
import org.eadge.gxscript.data.script.Func;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Real number creator
 */
public class RealEntity extends DefaultVariableEntity
{
    private float defaultFloat = 0;

    public RealEntity()
    {
        super("Create Real");

        addInputEntry("", Void.class);

        addInputEntryNotNeeded("Set", Float.class);

        addOutputEntry("Variable", Float.class);
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

    @Override
    public ModifyingEntity createModificationEntity(int outputIndex)
    {
        return new ModifyRealEntity();
    }
}
