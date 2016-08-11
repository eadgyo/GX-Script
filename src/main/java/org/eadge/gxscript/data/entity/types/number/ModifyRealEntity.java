package org.eadge.gxscript.data.entity.types.number;

import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Modifying real entity
 */
public class ModifyRealEntity extends ModifyingEntity
{
    @Override
    public Func getFunc()
    {
        return new Func()
        {
            @Override
            public void run(Program program)
            {

            }
        };
    }
}
