package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 10/08/16.
 *
 * Data address
 */
public class DataAddress extends Address
{
    public DataAddress()
    {
    }

    public DataAddress(int address)
    {
        super(address);
    }

    public DataAddress(DataAddress dataAddress)
    {
        this(dataAddress.getAddress());
    }

    public DataAddress clone()
    {
        return (DataAddress) super.clone();
    }

    public void setAddress(DataAddress address)
    {
        setAddress(address.getAddress());
    }

    public DataAddress addOffset(int offset)
    {
        return new DataAddress(getAddress() + offset);
    }

    public DataAddress addOffset(Address offset)
    {
        return new DataAddress(getAddress() + offset.getAddress());
    }
}
