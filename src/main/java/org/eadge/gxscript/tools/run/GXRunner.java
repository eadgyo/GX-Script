package org.eadge.gxscript.tools.run;

import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.Program;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Run GX compiled code
 */
public class GXRunner
{
    public void run(CompiledGXScript compiledGXScript)
    {
        // Create program memory and add pointer reading first function
        Program program = new Program(compiledGXScript.getCalledFunctions(), compiledGXScript.getCalledFunctionsParameters());

        // While there are functions left
        while (!program.hasFinished())
        {
            // Get function and corresponding parameters
            program.callCurrentFunc();

            // Go to next function
            program.nextFuncAddress();
        }
    }
}
