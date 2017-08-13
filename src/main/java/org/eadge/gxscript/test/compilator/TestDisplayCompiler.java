package org.eadge.gxscript.test.compilator;

import org.eadge.gxscript.data.compile.script.DisplayCompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScriptDebug;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.test.PrintTest;
import org.eadge.gxscript.tools.compile.GXCompilerDisplay;
import org.eadge.gxscript.tools.run.GXRunnerDisplay;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class TestDisplayCompiler
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test compilator display");

        PrintTest.printResult(testCorrect0(), "Check valid script, 1 level of imbrication");

    }

    private static boolean testCorrect0()
    {
        RawGXScriptDebug rawGXScript = CreateGXScript.createSimpleIf();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(System.out);

        GXCompilerDisplay       compiler = new GXCompilerDisplay();
        DisplayCompiledGXScript compile  = compiler.compile(rawGXScript);
        GXRunnerDisplay         gxRunner = new GXRunnerDisplay();
        gxRunner.run(compile, bufferedOutputStream);

        return true;
    }


}
