package org.eadge.gxscript.data.compile.script;

import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;

import java.util.ArrayList;
import java.util.Collection;

public class DisplayCompiledGXScript extends CompiledGXScript
{
    private ArrayList<String> funcNames;

    public DisplayCompiledGXScript(Collection<Class> inputsScriptClasses,
                                   Collection<Class> outputsScriptClasses,
                                   Collection<Func> calledFunctions,
                                   Collection<FuncDataAddresses> calledFunctionsParameters,
                                   ArrayList<String> funcNames)
    {
        super(inputsScriptClasses, outputsScriptClasses, calledFunctions, calledFunctionsParameters);
        this.funcNames = funcNames;
    }

    public ArrayList<String> getFuncNames()
    {
        return funcNames;
    }
}
