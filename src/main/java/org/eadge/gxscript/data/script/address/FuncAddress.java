package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 10/08/16.
 *
 * Func address
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

    public void setAddress(FuncAddress address)
    {
        setAddress(address.getAddress());
    }

    @Override
    public FuncAddress previous()
    {
        return new FuncAddress(getAddress() - 1);
    }

    @Override
    public FuncAddress next()
    {
        return new FuncAddress(getAddress() + 1);
    }

    public FuncAddress addOffset(int offset)
    {
        return new FuncAddress(getAddress() + offset);
    }

    public FuncAddress addOffset(Address offset)
    {
        return new FuncAddress(getAddress() + offset.getAddress());
    }
}
