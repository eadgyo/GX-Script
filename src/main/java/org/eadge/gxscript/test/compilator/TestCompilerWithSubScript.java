package org.eadge.gxscript.test.compilator;

import org.eadge.gxscript.data.compile.script.CompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.tools.compile.GXCompiler;
import org.eadge.gxscript.tools.run.GXRunner;

import java.io.IOException;

/**
 * Created by eadgyo on 03/03/17.
 *
 * Test creation and run of script containing compiled GX Script as entity
 */
public class TestCompilerWithSubScript
{
    public static void main(String[] args) throws IOException
    {
        RawGXScript fullScriptWithCompiledScript = CreateGXScript.createFullScriptWithCompiledScript();

        GXCompiler gxCompiler = new GXCompiler();
        CompiledGXScript compiledGXScript = gxCompiler.compile(fullScriptWithCompiledScript);

        GXRunner runner = new GXRunner();
        runner.run(compiledGXScript);
    }
}
