package org.eadge.gxscript.data.script.address;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eadgyo on 10/08/16.
 */
public class OutputAddresses
{
    private Map<Integer, Address> outputAddresses;

    public OutputAddresses()
    {
        outputAddresses = new HashMap<>();
    }

    public int numberOfOutputs()
    {
        return outputAddresses.size();
    }

    public Address getOutputAddress(int index)
    {
        return outputAddresses.get(index);
    }

    public void addOutputAddress(int index, Address address)
    {
        outputAddresses.put(index, address);
    }

    public void addOffset(Address offset)
    {
        Collection<Address> values = outputAddresses.values();
        for (Address address : values)
        {
            address.addOffset(offset);
        }
    }
}
