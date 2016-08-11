package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 10/08/16.
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

    public DataAddress clone()
    {
        return (DataAddress) super.clone();
    }

    public void setAddress(DataAddress address)
    {
        setAddress(address.getAddress());
    }
}
