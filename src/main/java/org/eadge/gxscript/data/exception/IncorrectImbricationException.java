package org.eadge.gxscript.data.exception;

import org.eadge.gxscript.data.entity.model.base.GXEntity;

/**
 * Created by eadgyo on 04/08/16.
 */
public class IncorrectImbricationException extends Exception
{
    private GXEntity GXEntity0, GXEntity1;

    public GXEntity getGXEntity0()
    {
        return GXEntity0;
    }

    public GXEntity getGXEntity1()
    {
        return GXEntity1;
    }

    public IncorrectImbricationException(GXEntity GXEntity0, GXEntity GXEntity1)
    {
        this.GXEntity0 = GXEntity0;
        this.GXEntity1 = GXEntity1;
    }
}
