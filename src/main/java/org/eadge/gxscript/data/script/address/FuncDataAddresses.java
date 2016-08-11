package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Holds data inputs addresses on memory stack, used to call function
 */
public class FuncDataAddresses
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

    public Address getInputAddress(int index)
    {
        return inputsAddresses[index];
    }

    public void setInputAddress(int index, DataAddress address)
    {
        inputsAddresses[index] = address;
    }
}
