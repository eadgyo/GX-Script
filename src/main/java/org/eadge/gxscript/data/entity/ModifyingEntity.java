package org.eadge.gxscript.data.entity;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Modifying variables entity model, they have only inputs
 * First function is the modified variable
 * Second function is the value for the modified variable
 */
public abstract class ModifyingEntity extends DefaultEntity
{
    public ModifyingEntity(String name)
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
