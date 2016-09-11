package org.eadge.gxscript.tools.run;

import org.eadge.gxscript.data.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.script.DebugProgram;

/**
 * Created by eadgyo on 02/08/16.
 *
 * GX Runner debug
 */
public class GXRunnerDebug extends GXRunner
{
    public void runDebug(DebugCompiledGXScript compiledGXScript)
    {
        // Create program memory and add pointer reading first function
        DebugProgram program = new DebugProgram(compiledGXScript.getCalledFunctions(),
                                                compiledGXScript.getCalledFunctionsParameters(),
                                                compiledGXScript.getDebugMemories());

        // While there are functions left
        while (!program.hasFinished())
        {
            // Call the current pointed function
            program.callCurrentFunc();

            // Go to next function
            program.nextFuncAddress();
        }
    }
}
