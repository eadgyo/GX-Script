package org.eadge.gxscript.data.compile.script;

import org.eadge.gxscript.data.compile.script.address.DebugMemory;
import org.eadge.gxscript.data.compile.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.compile.script.func.Func;

import java.util.Collection;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Debug compiled GXScript
 */
public class DebugCompiledGXScript extends CompiledGXScript
{
    private DebugMemory[] debugMemories;

    public DebugCompiledGXScript(Collection<Class> inputsScriptClasses,
                                 Collection<Class> outputsScriptClasses,
                                 Collection<String> inputsScriptNames,
                                 Collection<String> outputsScriptNames,
                                 Collection<Func> calledFunctions,
                                 Collection<FuncDataAddresses> calledFunctionsParameters,
                                 Collection<DebugMemory> debugMemories)
    {
        super(inputsScriptClasses, outputsScriptClasses, inputsScriptNames, outputsScriptNames, calledFunctions,
              calledFunctionsParameters);

        this.debugMemories = new DebugMemory[debugMemories.size()];
        debugMemories.toArray(this.debugMemories);
    }

    public DebugMemory[] getDebugMemories()
    {
        return debugMemories;
    }
}
