package org.eadge.gxscript.data.entity.types.number;

import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.func.types.number.ModifyRealFunc;
import org.eadge.gxscript.data.script.Func;

/**
 * Created by eadgyo on 11/08/16.
 */
public class ModifyRealEntity extends ModifyingEntity
{
    @Override
    public Func getFunc()
    {
        return new ModifyRealFunc();
    }
}
