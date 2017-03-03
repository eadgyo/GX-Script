package org.eadge.gxscript.data.script.address;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eadgyo on 10/08/16.
 *
 * Output addresses mapped to output indices
 */
public class OutputAddresses
{
    private Map<Integer, DataAddress> outputAddresses;

    public OutputAddresses()
    {
        outputAddresses = new HashMap<>();
    }

    public int numberOfOutputs()
    {
        return outputAddresses.size();
    }

    public DataAddress getOutputAddress(int index)
    {
        return outputAddresses.get(index);
    }

    public void addOutputAddress(int index, DataAddress address)
    {
        outputAddresses.put(index, address);
    }

    public void addOffset(Address offset)
    {
        Collection<DataAddress> values = outputAddresses.values();
        for (Address address : values)
        {
            address.selfAddOffset(offset);
        }
    }

    public void addOffset(int offset)
    {
        Collection<DataAddress> values = outputAddresses.values();
        for (Address address : values)
        {
            address.selfAddOffset(offset);
        }
    }
}
