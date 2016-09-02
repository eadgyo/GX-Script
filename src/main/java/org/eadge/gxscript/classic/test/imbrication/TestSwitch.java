package org.eadge.gxscript.classic.test.imbrication;

import org.eadge.gxscript.classic.entity.displayer.PrintEntity;
import org.eadge.gxscript.classic.entity.imbrication.conditionals.SwitchEntity;
import org.eadge.gxscript.classic.entity.types.number.IntEntity;
import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.RawGXScriptDebug;
import org.eadge.gxscript.tools.check.GXValidator;
import org.eadge.gxscript.tools.compile.GXCompiler;
import org.eadge.gxscript.tools.run.GXRunner;

import java.io.IOException;

/**
 * Created by eadgyo on 02/09/16.
 *
 * Test switch imbrication process
 */
public class TestSwitch
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test switch");

        testSwitch();
    }

    public static void testSwitch()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create switch component
        SwitchEntity switchEntity = new SwitchEntity();

        // Create source entity
        IntEntity intEntity = new IntEntity(3);

        // Link source to switch
        switchEntity.linkAsInput(SwitchEntity.SOURCE_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);

        PrintEntity printEntityCont = new PrintEntity("Continue");
        printEntityCont.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, switchEntity.getContinueOutputIndex(), switchEntity);

        // Link 0 to printEntity0
        PrintEntity printEntity0 = new PrintEntity("0");
        printEntity0.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex() + 1, switchEntity);

        // Add 1 as output
        switchEntity.addOutputCase(1);

        // Link 1 to printEntity1
        PrintEntity printEntity1 = new PrintEntity("1");
        printEntity1.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex() + 2, switchEntity);

        // Add 3 as output corresponding to 3rd value and change it to 4
        switchEntity.addOutputCase(3);
        switchEntity.setOutputCaseValue(2, 5); // last added is 2

        // Link 5 to printEntity5
        PrintEntity printEntity5 = new PrintEntity("5");
        printEntity5.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex() + 3, switchEntity);

        // Link def to printEntityDef
        PrintEntity printEntityDef = new PrintEntity("Def");
        printEntityDef.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex(), switchEntity);

        rawGXScript.addEntity("Cont", printEntityCont);
        rawGXScript.addEntity("p0", printEntity0);
        rawGXScript.addEntity("p1", printEntity1);
        rawGXScript.addEntity("p5", printEntity5);
        rawGXScript.addEntity("Def", printEntityDef);
        rawGXScript.addEntity("switch", switchEntity);
        rawGXScript.addEntity("int0", intEntity);

        GXValidator validator = new GXValidator();
        if (validator.validate(rawGXScript))
        {
            GXCompiler compiler = new GXCompiler();
            CompiledGXScript compiledGXScript = compiler.compile(rawGXScript);

            GXRunner runner = new GXRunner();
            runner.run(compiledGXScript);
        }
        else
        {
            System.out.println("Problem");
        }
    }
}
