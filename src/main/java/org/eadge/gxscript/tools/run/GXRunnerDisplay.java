package org.eadge.gxscript.tools.run;

import org.eadge.gxscript.data.compile.program.DisplayProgram;
import org.eadge.gxscript.data.compile.script.DisplayCompiledGXScript;

import java.io.OutputStream;

public class GXRunnerDisplay
{
    public void run(DisplayCompiledGXScript compiledGXScript, OutputStream outputStream)
    {
        // Create program memory and add pointer reading first script
        DisplayProgram program = new DisplayProgram(compiledGXScript.getCalledFunctions(), compiledGXScript.getCalledFunctionsAddresses
                (), compiledGXScript.getFuncNames(), outputStream);

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
