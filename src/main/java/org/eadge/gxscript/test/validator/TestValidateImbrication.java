package org.eadge.gxscript.test.validator;

import org.eadge.gxscript.data.entity.classic.entity.displayer.PrintGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.imbrication.conditionals.IfGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.types.number.RealGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.classic.entity.imbrication.loops.ForGXEntity;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScriptDebug;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.test.PrintTest;
import org.eadge.gxscript.tools.check.validator.ValidateImbrication;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.io.IOException;

/**
 * Created by eadgyo on 12/08/16.
 *
 * Test to validate imbrication
 */
public class TestValidateImbrication
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test validate imbrications");

        PrintTest.printResult(testCorrect0(), "Check valid script, 1 level of imbrication");
        PrintTest.printResult(testCorrect1(), "Check valid script, 1 level of imbrication");
        PrintTest.printResult(testCorrect2(), "Check valid script, 2 levels of imbrications, 2 parallels");
        PrintTest.printResult(testCorrect3(), "Check valid script, 2 levels of imbrications, 2 parallels");

        PrintTest.printResult(testNotCorrectScript0(), "Check not valid script, 1 level of imbrication");
        PrintTest.printResult(testNotCorrectScript1(), "Check not valid script, 1 level of imbrication");
        PrintTest.printResult(testNotCorrectScript2(), "Check not valid script, 2 levels of imbrications, 2 parallels");
        PrintTest.printResult(testNotCorrectScript3(), "Check not valid script, 2 levels of imbrications, 2 parallels");
        PrintTest.printResult(testNotCorrectScript4(), "Check not valid script, 2 levels of imbrications, 2 parallels");
    }

    public static boolean testCorrect0()
    {
        RawGXScript script = CreateGXScript.createSimpleIf();

        ValidatorModel validator = new ValidateImbrication();
        return validator.validate(script);
    }

    public static boolean testCorrect1()
    {
        RawGXScript script = CreateGXScript.createScriptIf();

        ValidatorModel validator = new ValidateImbrication();
        return validator.validate(script);
    }

    public static boolean testCorrect2()
    {
        RawGXScript script = CreateGXScript.createComplexScript();

        ValidatorModel validator = new ValidateImbrication();
        return validator.validate(script);
    }

    public static boolean testCorrect3()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        GXEntity forGXEntity = script.getEntity("for");

        // Create real GXEntity
        RealGXEntity realEntity = new RealGXEntity(10f);
        forGXEntity.linkAsInput(ForGXEntity.NEXT_INPUT_INDEX, RealGXEntity.CONTINUE_OUTPUT_INDEX, realEntity);

        // Create print in imbrication
        PrintGXEntity printEntity = new PrintGXEntity();
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, ForGXEntity.DO_OUTPUT_INDEX, forGXEntity);

        // Link index of in imbrication with print GXEntity after imbrication
        printEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real1", realEntity);
        script.addEntity("print", printEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateImbrication();
        return validator.validate(script);
    }

    public static boolean testNotCorrectScript0()
    {
        RawGXScriptDebug script = CreateGXScript.createScriptIf();

        // Get if
        GXEntity ifGXEntity = script.getEntity("if");

        // Create a new real in first imbrication
        RealGXEntity realEntity = new RealGXEntity(10f);
        realEntity.linkAsInput(RealGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifGXEntity);

        // Create a printEntity in second imbrication
        PrintGXEntity printEntity = new PrintGXEntity();
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.FAIL_OUTPUT_INDEX, ifGXEntity);

        // Link first to second imbrication using real in first and print in second
        printEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript1()
    {
        RawGXScriptDebug script = CreateGXScript.createScriptIf();

        // Get if
        GXEntity ifGXEntity = script.getEntity("if");

        // Create a new real in first imbrication
        RealGXEntity realEntity = new RealGXEntity(10f);
        realEntity.linkAsInput(RealGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifGXEntity);

        // Create a printEntity after imbrications
        PrintGXEntity printEntity = new PrintGXEntity();
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.CONTINUE_OUTPUT_INDEX, ifGXEntity);

        // Link first to second imbrication using real in first and print after imbrications
        printEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript2()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        GXEntity ifGXEntity  = script.getEntity("if");
        GXEntity forGXEntity = script.getEntity("for");

        // Create real in if success imbrication
        RealGXEntity realEntity = new RealGXEntity(10f);
        realEntity.linkAsInput(RealGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifGXEntity);

        // Create print in for after imbrication
        PrintGXEntity printEntity = new PrintGXEntity();
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, ForGXEntity.CONTINUE_OUTPUT_INDEX, forGXEntity);

        // Link second level to after imbrication using real in first and print after imbrications
        printEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript3()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        GXEntity ifGXEntity  = script.getEntity("if");
        GXEntity forGXEntity = script.getEntity("for");

        // Create real in if success imbrication
        RealGXEntity realEntity = new RealGXEntity(10f);
        realEntity.linkAsInput(RealGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifGXEntity);

        // DIFFERENCE TO 2 -> add GXEntity between print and create real

        // Create print in for after imbrication
        PrintGXEntity printEntity0 = new PrintGXEntity();
        PrintGXEntity printEntity  = new PrintGXEntity();
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, PrintGXEntity.CONTINUE_OUTPUT_INDEX, printEntity0);
        printEntity0.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, ForGXEntity.CONTINUE_OUTPUT_INDEX, forGXEntity);

        // Link second level to after imbrication using real in first and print after imbrications
        printEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript4()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        GXEntity forGXEntity = script.getEntity("for");

        // Create print after imbrication
        PrintGXEntity printEntity = new PrintGXEntity();
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, ForGXEntity.CONTINUE_OUTPUT_INDEX, forGXEntity);

        // Link index with print GXEntity after imbrication
        printEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, ForGXEntity.INDEX_FOR_OUTPUT_INDEX, forGXEntity);

        // Add them in script and update
        script.addEntity("print", printEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }
}
