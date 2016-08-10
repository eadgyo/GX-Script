package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 10/08/16.
 */
public class FuncAddress extends Address
{
    public FuncAddress()
    {
    }

    public FuncAddress(int address)
    {
        super(address);
    }

    public FuncAddress clone()
    {
        return (FuncAddress) super.clone();
    }
}
