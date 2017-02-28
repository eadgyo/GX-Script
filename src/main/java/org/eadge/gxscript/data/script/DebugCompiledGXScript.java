package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.script.address.DebugMemory;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;

import java.util.Collection;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Debug compiled GXScript
 */
public class DebugCompiledGXScript extends CompiledGXScript
{
    private DebugMemory[] debugMemories;

    public DebugCompiledGXScript(int numberOfScriptParameters,
                                 Collection<Func> calledFunctions,
                                 Collection<FuncDataAddresses> calledFunctionsParameters,
                                 Collection<DebugMemory> debugMemories)
    {
        super(numberOfScriptParameters, calledFunctions, calledFunctionsParameters);

        this.debugMemories = new DebugMemory[debugMemories.size()];
        debugMemories.toArray(this.debugMemories);
    }

    public DebugMemory[] getDebugMemories()
    {
        return debugMemories;
    }
}
