package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 10/08/16.
 *
 */
public class Address implements Cloneable
{
    private int address;

    public Address clone()
    {
        try
        {
            return (Address) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Address()
    {

    }

    public Address(int address)
    {
        this.address = address;
    }

    public int getAddress()
    {
        return address;
    }

    public void setAddress(int address)
    {
        this.address = address;
    }

    public void alloc()
    {
        address += 1;
    }

    public void desalloc()
    {
        address -= 1;
    }

    public void addOffset(int offset)
    {
        address += offset;
    }

    public void addOffset(Address offset)
    {
        address += offset.getAddress();
    }
}
