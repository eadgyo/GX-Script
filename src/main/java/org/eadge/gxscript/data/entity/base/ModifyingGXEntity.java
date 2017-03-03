package org.eadge.gxscript.data.entity.base;

import org.eadge.gxscript.data.entity.def.DefaultGXEntity;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Modifying variables GXEntity model, they have only inputs
 * First script is the modified variable
 * Second script is the value for the modified variable
 */
public abstract class ModifyingGXEntity extends DefaultGXEntity
{
    public ModifyingGXEntity(String name)
    {
        super(name);
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        // Unlink all inputs
        super.unlinkAsInput(0);
        super.unlinkAsInput(1);
    }
}
