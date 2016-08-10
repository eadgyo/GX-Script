package org.eadge.gxscript.data.script.address;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Function used inputs and outputs used addresses
 */
public class FuncDataAddresses
{
    private Address[] inputsAddresses;

    public FuncDataAddresses(int numberOfInputs)
    {
        inputsAddresses = new Address[numberOfInputs];
    }

    public int numberOfInputs()
    {
        return inputsAddresses.length;
    }

    public Address getInputAddress(int index)
    {
        return inputsAddresses[index];
    }

    public void setInputAddress(int index, Address address)
    {
        inputsAddresses[index] = address;
    }
}
