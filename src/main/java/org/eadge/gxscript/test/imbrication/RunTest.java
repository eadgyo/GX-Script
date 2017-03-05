package org.eadge.gxscript.test.imbrication;

import org.eadge.gxscript.data.compile.script.DebugCompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.tools.check.GXValidator;
import org.eadge.gxscript.tools.compile.GXCompilerDebug;
import org.eadge.gxscript.tools.run.GXRunnerDebug;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Run test on code
 */
public class RunTest
{
    public static boolean run(RawGXScript rawGXScript)
    {
        GXValidator validator = new GXValidator();
        if (validator.validate(rawGXScript))
        {
            GXCompilerDebug       compiler         = new GXCompilerDebug();
            DebugCompiledGXScript compiledGXScript = compiler.compile(rawGXScript);

            GXRunnerDebug runner = new GXRunnerDebug();
            runner.runDebug(compiledGXScript);
            return true;
        }
        else
        {
            return false;
        }
    }
}
