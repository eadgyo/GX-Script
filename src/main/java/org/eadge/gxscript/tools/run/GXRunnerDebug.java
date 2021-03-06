package org.eadge.gxscript.tools.run;

import org.eadge.gxscript.data.compile.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.compile.program.DebugProgram;

/**
 * Created by eadgyo on 02/08/16.
 *
 * GX Runner debug
 */
public class GXRunnerDebug extends GXRunner
{
    public void runDebug(DebugCompiledGXScript compiledGXScript)
    {
        // Create program memory and add pointer reading first script
        DebugProgram program = new DebugProgram(compiledGXScript.getCalledFunctions(),
                                                compiledGXScript.getCalledFunctionsAddresses(),
                                                compiledGXScript.getDebugMemories());

        // While there are functions left
        while (!program.hasFinished())
        {
            // Call the current pointed script
            program.callCurrentFunc();

            // Go to next script
            program.nextFuncAddress();
        }
    }
}
