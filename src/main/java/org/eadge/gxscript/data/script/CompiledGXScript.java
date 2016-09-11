package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.FuncDataAddresses;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Debug compiled GXScript
 */
public class CompiledGXScript
{
    private Func calledFunctions[];
    private FuncDataAddresses calledFunctionsParameters[];

    public CompiledGXScript(Collection<Func> calledFunctions, Collection<FuncDataAddresses> calledFunctionsParameters)
    {
        assert (calledFunctions.size() == calledFunctionsParameters.size());

        this.calledFunctions = new Func[calledFunctions.size()];
        this.calledFunctionsParameters = new FuncDataAddresses[calledFunctions.size()];

        calledFunctions.toArray(this.calledFunctions);
        calledFunctionsParameters.toArray(this.calledFunctionsParameters);
    }

    public Func[] getCalledFunctions()
    {
        return calledFunctions;
    }

    public FuncDataAddresses[] getCalledFunctionsParameters()
    {
        return calledFunctionsParameters;
    }
}
