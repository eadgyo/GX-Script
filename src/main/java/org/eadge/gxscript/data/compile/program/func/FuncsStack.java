package org.eadge.gxscript.data.compile.program.func;

import org.eadge.gxscript.data.compile.script.address.FuncAddress;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.address.FuncImbricationDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;

/**
 * Created by eadgyo on 11/08/16.
 *
 * Holds called funcs and funcs parameters
 */
public class FuncsStack
{
    private Func              funcs[];
    private FuncDataAddresses funcDataAddresses[];

    public FuncsStack(Func[] funcs, FuncDataAddresses[] funcDataAddresses)
    {
        assert (funcs.length == funcDataAddresses.length);

        this.funcs = funcs;
        this.funcDataAddresses = funcDataAddresses;
    }

    public Func getFunc(FuncAddress funcAddress)
    {
        return funcs[funcAddress.getAddress()];
    }

    public FuncDataAddresses getFuncParameters(FuncAddress funcAddress)
    {
        return funcDataAddresses[funcAddress.getAddress()];
    }

    public FuncImbricationDataAddresses getFuncImbricationParameters(FuncAddress funcAddress)
    {
        return (FuncImbricationDataAddresses) getFuncParameters(funcAddress);
    }

    public int size()
    {
        return funcs.length;
    }
}
