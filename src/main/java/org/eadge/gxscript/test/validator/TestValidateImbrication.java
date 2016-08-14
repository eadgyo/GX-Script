package org.eadge.gxscript.test.validator;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.displayer.PrintEntity;
import org.eadge.gxscript.data.entity.imbrication.conditionals.IfEntity;
import org.eadge.gxscript.data.entity.imbrication.loops.ForEntity;
import org.eadge.gxscript.data.entity.types.number.RealEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.data.script.RawGXScriptDebug;
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

        Entity forEntity = script.getEntity("for");

        // Create real entity
        RealEntity realEntity = new RealEntity(10f);
        forEntity.linkAsInput(ForEntity.NEXT_INPUT_INDEX, RealEntity.CONTINUE_OUTPUT_INDEX, realEntity);

        // Create print in imbrication
        PrintEntity printEntity = new PrintEntity();
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, ForEntity.DO_INPUT_INDEX, forEntity);

        // Link index of in imbrication with print entity after imbrication
        printEntity.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real1", realEntity);
        script.addEntity("print", printEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateImbrication();
        return validator.validate(script);
    }

    public static boolean testNotCorrectScript0()
    {
        RawGXScriptDebug script = CreateGXScript.createScriptIf();

        // Get if
        Entity ifEntity = script.getEntity("if");

        // Create a new real in first imbrication
        RealEntity realEntity = new RealEntity(10f);
        realEntity.linkAsInput(RealEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        // Create a printEntity in second imbrication
        PrintEntity printEntity = new PrintEntity();
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.FAIL_OUTPUT_INDEX, ifEntity);

        // Link first to second imbrication using real in first and print in second
        printEntity.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript1()
    {
        RawGXScriptDebug script = CreateGXScript.createScriptIf();

        // Get if
        Entity ifEntity = script.getEntity("if");

        // Create a new real in first imbrication
        RealEntity realEntity = new RealEntity(10f);
        realEntity.linkAsInput(RealEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        // Create a printEntity after imbrications
        PrintEntity printEntity = new PrintEntity();
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.CONTINUE_OUTPUT_INDEX, ifEntity);

        // Link first to second imbrication using real in first and print after imbrications
        printEntity.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript2()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        Entity ifEntity = script.getEntity("if");
        Entity forEntity = script.getEntity("for");

        // Create real in if success imbrication
        RealEntity realEntity = new RealEntity(10f);
        realEntity.linkAsInput(RealEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        // Create print in for after imbrication
        PrintEntity printEntity = new PrintEntity();
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, ForEntity.CONTINUE_OUTPUT_INDEX, forEntity);

        // Link second level to after imbrication using real in first and print after imbrications
        printEntity.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript3()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        Entity ifEntity = script.getEntity("if");
        Entity forEntity = script.getEntity("for");

        // Create real in if success imbrication
        RealEntity realEntity = new RealEntity(10f);
        realEntity.linkAsInput(RealEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        // DIFFERENCE TO 2 -> add entity between print and create real

        // Create print in for after imbrication
        PrintEntity printEntity0 = new PrintEntity();
        PrintEntity printEntity = new PrintEntity();
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, PrintEntity.CONTINUE_OUTPUT_INDEX, printEntity0);
        printEntity0.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, ForEntity.CONTINUE_OUTPUT_INDEX, forEntity);

        // Link second level to after imbrication using real in first and print after imbrications
        printEntity.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity);

        // Add them in script and update
        script.addEntity("real4", realEntity);
        script.addEntity("print", printEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectScript4()
    {
        RawGXScriptDebug script = CreateGXScript.createComplexScript();

        Entity forEntity = script.getEntity("for");

        // Create print after imbrication
        PrintEntity printEntity = new PrintEntity();
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, ForEntity.CONTINUE_OUTPUT_INDEX, forEntity);

        // Link index with print entity after imbrication
        printEntity.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, ForEntity.INDEX_FOR_OUTPUT_INDEX, forEntity);

        // Add them in script and update
        script.addEntity("print", printEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateImbrication();
        return !validator.validate(script);
    }
}
