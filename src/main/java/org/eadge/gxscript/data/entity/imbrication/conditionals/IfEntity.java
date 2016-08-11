package org.eadge.gxscript.data.entity.imbrication.conditionals;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.script.Func;

/**
 * Created by eadgyo on 02/08/16.
 *
 * If entity block
 */
public class IfEntity extends DefaultEntity
{
    public IfEntity()
    {
        addInputEntry(Boolean.class);

        // Success
        addOutputEntry(Void.class);

        // Fail
        addOutputEntry(Void.class);
    }

    @Override
    public Func getFunc()
    {
        return null;
    }
}
