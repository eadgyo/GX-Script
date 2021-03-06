package org.eadge.gxscript.test.compilator;

import org.eadge.gxscript.data.compile.script.CompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScriptDebug;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.test.PrintTest;
import org.eadge.gxscript.tools.compile.GXCompiler;
import org.eadge.gxscript.tools.run.GXRunner;

import java.io.IOException;

/**
 * Created by eadgyo on 13/08/16.
 *
 * Test compiler
 */
public class TestCompiler
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test compilator");

        PrintTest.printResult(testCorrect0(), "Check valid script, 1 level of imbrication");
        PrintTest.printResult(testCorrect1(), "Check valid script, 1 level of imbrication");

        PrintTest.printResult(testCorrect2(), "Check valid script, 2 levels of imbrication");
        PrintTest.printResult(testCorrect3(), "Check valid script, 1 levels of imbrication");

    }

    private static boolean testCorrect0()
    {
        RawGXScriptDebug rawGXScript = CreateGXScript.createSimpleIf();

        GXCompiler compiler = new GXCompiler();
        CompiledGXScript compile = compiler.compile(rawGXScript);
        GXRunner gxRunner = new GXRunner();
        gxRunner.run(compile);

        return true;
    }

    private static boolean testCorrect1()
    {
        RawGXScriptDebug rawGXScript = CreateGXScript.createScriptIf();

        GXCompiler compiler = new GXCompiler();
        CompiledGXScript compile = compiler.compile(rawGXScript);
        GXRunner gxRunner = new GXRunner();
        gxRunner.run(compile);

        return true;
    }

    private static boolean testCorrect2()
    {
        RawGXScriptDebug rawGXScript = CreateGXScript.createComplexScript();

        GXCompiler compiler = new GXCompiler();
        CompiledGXScript compile = compiler.compile(rawGXScript);
        GXRunner gxRunner = new GXRunner();
        gxRunner.run(compile);

        return true;
    }

    private static boolean testCorrect3()
    {
        RawGXScript rawGXScript = CreateGXScript.createScriptFor();

        GXCompiler compiler = new GXCompiler();
        CompiledGXScript compile = compiler.compile(rawGXScript);
        GXRunner gxRunner = new GXRunner();
        gxRunner.run(compile);

        return true;
    }
}
