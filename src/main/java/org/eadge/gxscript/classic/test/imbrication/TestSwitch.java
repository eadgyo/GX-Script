package org.eadge.gxscript.classic.test.imbrication;

import org.eadge.gxscript.classic.entity.displayer.PrintGXEntity;
import org.eadge.gxscript.classic.entity.imbrication.conditionals.SwitchGXEntity;
import org.eadge.gxscript.classic.entity.types.number.IntGXEntity;
import org.eadge.gxscript.data.script.RawGXScriptDebug;
import org.eadge.gxscript.test.PrintTest;

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

        PrintTest.printResult(test(), "Check switch script");;
    }

    public static boolean test()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create switch component
        SwitchGXEntity switchEntity = new SwitchGXEntity();

        // Create source GXEntity
        IntGXEntity intEntity = new IntGXEntity(3);

        // Link source to switch
        switchEntity.linkAsInput(SwitchGXEntity.SOURCE_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);

        PrintGXEntity printEntityCont = new PrintGXEntity("Continue");
        printEntityCont.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, switchEntity.getContinueOutputIndex(), switchEntity);

        // Link 0 to printEntity0
        PrintGXEntity printEntity0 = new PrintGXEntity("0");
        printEntity0.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex() + 1, switchEntity);

        // Add 1 as output
        switchEntity.addOutputCase(1);

        // Link 1 to printEntity1
        PrintGXEntity printEntity1 = new PrintGXEntity("1");
        printEntity1.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex() + 2, switchEntity);

        // Add 3 as output corresponding to 3rd value and change it to 4
        switchEntity.addOutputCase(3);
        switchEntity.setOutputCaseValue(2, 5); // last added is 2

        // Link 5 to printEntity5
        PrintGXEntity printEntity5 = new PrintGXEntity("5");
        printEntity5.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex() + 3, switchEntity);

        // Link def to printEntityDef
        PrintGXEntity printEntityDef = new PrintGXEntity("Def");
        printEntityDef.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, switchEntity.getDefaultOutputIndex(), switchEntity);

        rawGXScript.addEntity("Cont", printEntityCont);
        rawGXScript.addEntity("p0", printEntity0);
        rawGXScript.addEntity("p1", printEntity1);
        rawGXScript.addEntity("p5", printEntity5);
        rawGXScript.addEntity("Def", printEntityDef);
        rawGXScript.addEntity("switch", switchEntity);
        rawGXScript.addEntity("int0", intEntity);

        return RunTest.run(rawGXScript);
    }
}
