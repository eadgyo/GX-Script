package org.eadge.gxscript.data.compile.script.address;

import java.io.Serializable;

/**
 * Created by eadgyo on 10/08/16.
 *
 */
public class Address implements Cloneable, Serializable
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

    public void selfAddOffset(int offset)
    {
        address += offset;
    }

    public void selfAddOffset(Address offset)
    {
        address += offset.getAddress();
    }

    public Address addOffset(int offset)
    {
        return new Address(address + offset);
    }

    public Address addOffset(Address offset)
    {
        return new Address(address + offset.getAddress());
    }

    public boolean isAfter(FuncAddress testedFuncAddress)
    {
        return address > testedFuncAddress.getAddress();
    }

    public boolean isBefore(FuncAddress testedFuncAddress)
    {
        return address < testedFuncAddress.getAddress();
    }

    public boolean equals(FuncAddress testedFuncAddress)
    {
        return address == testedFuncAddress.getAddress();
    }

    public Address previous()
    {
        return new Address(address - 1);
    }

    public Address next()
    {
        return new Address(address + 1);
    }

    @Override
    public String toString()
    {
        return "Address{" +
                "address=" + address +
                '}';
    }
}
