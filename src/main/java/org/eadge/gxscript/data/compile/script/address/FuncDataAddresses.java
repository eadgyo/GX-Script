package org.eadge.gxscript.data.compile.script.address;

import java.io.Serializable;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Holds data inputs addresses on memory stack, used to call script
 */
public class FuncDataAddresses implements Serializable
{
    private DataAddress[] inputsAddresses;

    public FuncDataAddresses(int numberOfInputs)
    {
        inputsAddresses = new DataAddress[numberOfInputs];
    }

    public int numberOfInputs()
    {
        return inputsAddresses.length;
    }

    public DataAddress getInputAddress(int index)
    {
        return inputsAddresses[index];
    }

    public void setInputAddress(int index, DataAddress address)
    {
        inputsAddresses[index] = address;
    }
}
