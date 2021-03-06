package org.eadge.gxscript.data.compile.script.address;

/**
 * Created by eadgyo on 10/08/16.
 *
 */
public class FuncImbricationDataAddresses extends FuncDataAddresses
{
    private FuncAddress[] startImbricationsFuncAddresses;

    public FuncImbricationDataAddresses(int numberOfInputs, int numberOfParallelsImbrications)
    {
        super(numberOfInputs);
        startImbricationsFuncAddresses = new FuncAddress[numberOfParallelsImbrications + 1];
    }

    public int numberOfImbrications()
    {
        return startImbricationsFuncAddresses.length;
    }

    public FuncAddress getImbricationAddress(int index)
    {
        return startImbricationsFuncAddresses[index];
    }

    public void setImbricationAddress(int index, FuncAddress address)
    {
        startImbricationsFuncAddresses[index] = address;
    }

    public void addOffsetFuncs(Address offset)
    {
        for (FuncAddress startImbricationsFuncAddress : startImbricationsFuncAddresses)
        {
            startImbricationsFuncAddress.selfAddOffset(offset);
        }
    }

    public void addOffsetFuncs(int offset)
    {
        for (FuncAddress startImbricationsFuncAddress : startImbricationsFuncAddresses)
        {
            startImbricationsFuncAddress.selfAddOffset(offset);
        }
    }

    public void setImbricationsAddresses(FuncAddress imbricationsAddresses[])
    {
        assert (imbricationsAddresses.length == numberOfImbrications());

        for (int imbricationIndex = 0; imbricationIndex < imbricationsAddresses.length; imbricationIndex++)
        {
            startImbricationsFuncAddresses[imbricationIndex] = imbricationsAddresses[imbricationIndex];
        }
    }
}
