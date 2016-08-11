package org.eadge.gxscript.data.entity.imbrication.conditionals;

import org.eadge.gxscript.data.entity.DefaultStartImbricationEntity;
import org.eadge.gxscript.data.func.conditionals.IfFunc;
import org.eadge.gxscript.data.script.Func;

/**
 * Created by eadgyo on 02/08/16.
 *
 * If entity block
 */
public class IfEntity extends DefaultStartImbricationEntity
{
    public IfEntity()
    {
        super("If");

        addInputEntry("Test", Boolean.class);

        // Success
        addImbricatedOutputEntry(0, "Success", Void.class);

        // Fail
        addImbricatedOutputEntry(1, "Fail", Void.class);

        // Continue
        addOutputEntry("Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        return new IfFunc();
    }
}
