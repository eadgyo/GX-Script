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
    /**
     * Keeps called functions in script
     */
    private Func              calledFunctions[];

    /**
     * Keeps parameters used with functions
     */
    private FuncDataAddresses calledFunctionsParameters[];

    /**
     * Number of parameters used for script
     */
    private int           numberOfScriptParameters;

    public CompiledGXScript(int numberOfScriptParameters, Collection<Func> calledFunctions, Collection<FuncDataAddresses> calledFunctionsParameters)
    {
        assert (calledFunctions.size() == calledFunctionsParameters.size());

        this.numberOfScriptParameters = numberOfScriptParameters;

        this.calledFunctions = new Func[calledFunctions.size()];
        this.calledFunctionsParameters = new FuncDataAddresses[calledFunctions.size()];

        calledFunctions.toArray(this.calledFunctions);
        calledFunctionsParameters.toArray(this.calledFunctionsParameters);
    }

    public int getNumberOfScriptParameters()
    {
        return numberOfScriptParameters;
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
