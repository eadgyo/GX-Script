package org.eadge.gxscript.test.validator;

import org.eadge.gxscript.classic.entity.displayer.PrintEntity;
import org.eadge.gxscript.classic.entity.imbrication.conditionals.IfEntity;
import org.eadge.gxscript.classic.entity.types.number.RealEntity;
import org.eadge.gxscript.classic.entity.types.number.comparison.EqualToNumberEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.test.PrintTest;
import org.eadge.gxscript.tools.check.validator.ValidateNoInterdependency;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.io.IOException;

/**
 * Created by eadgyo on 12/08/16.
 *
 * Test detection of interdependency
 */
public class TestValidateNoInterdependency
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test validate no interdependency");
        PrintTest.printResult(testCorrect(), "Check valid script");
        PrintTest.printResult(testInterdependency0(), "Check script with interdependency 0");
        PrintTest.printResult(testInterdependency1(), "Check script with interdependency 1");
    }

    public static boolean testCorrect()
    {
        RawGXScript scriptIf = CreateGXScript.createScriptIf();

        ValidatorModel validator = new ValidateNoInterdependency();
        return validator.validate(scriptIf);
    }

    public static boolean testInterdependency0()
    {
        RawGXScript script = new RawGXScript();

        // Create new entity
        RealEntity realEntity = new RealEntity();

        // Create start entity
        PrintEntity printEntity = new PrintEntity("Test");

        // Add direct self interdependency link
        realEntity.linkAsInput(RealEntity.SET_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity);
        realEntity.linkAsInput(RealEntity.NEXT_INPUT_INDEX, PrintEntity.CONTINUE_OUTPUT_INDEX, printEntity);

        // Update starting entities
        script.addEntity(printEntity);
        script.addEntity(realEntity);

        script.updateStartingEntities();

        ValidatorModel validator = new ValidateNoInterdependency();
        return !validator.validate(script);
    }

    public static boolean testInterdependency1()
    {
        // Create entities
        RawGXScript script = new RawGXScript();

        // Create 3 real variables
        RealEntity realEntity1 = new RealEntity(20f);
        RealEntity realEntity2 = new RealEntity(10f);
        RealEntity realEntity3 = new RealEntity();

        // Link realEntity2 on realEntity3 input
        realEntity3.linkAsInput(RealEntity.SET_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create real comparison
        EqualToNumberEntity equalToNumberEntity = new EqualToNumberEntity();
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V0_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity1);
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V1_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create if entity block control
        IfEntity ifEntity = new IfEntity();
        ifEntity.linkAsInput(IfEntity.TEST_INPUT_INDEX, EqualToNumberEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        // Create 3 prints for each path
        PrintEntity success = new PrintEntity("Success");
        success.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        PrintEntity fail = new PrintEntity("Fail");
        fail.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.FAIL_OUTPUT_INDEX, ifEntity);

        PrintEntity continueP = new PrintEntity("Continue");
        continueP.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.CONTINUE_OUTPUT_INDEX, ifEntity);

        // Add all created entities to raw gx script
        script.addEntity(realEntity1);
        script.addEntity(realEntity2);
        script.addEntity(realEntity3);
        script.addEntity(equalToNumberEntity);
        script.addEntity(ifEntity);
        script.addEntity(success);
        script.addEntity(fail);
        script.addEntity(continueP);


        // Addd interdepency one imbricated and the first added entity
        realEntity1.linkAsInput(RealEntity.NEXT_INPUT_INDEX, PrintEntity.CONTINUE_OUTPUT_INDEX, success);

        ValidatorModel validator = new ValidateNoInterdependency();
        return !validator.validate(script);
    }

}
