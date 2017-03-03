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
        // Create program memory and add pointer reading first script
        Program program = new Program(compiledGXScript.getCalledFunctions(), compiledGXScript.getCalledFunctionsAddresses());

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
