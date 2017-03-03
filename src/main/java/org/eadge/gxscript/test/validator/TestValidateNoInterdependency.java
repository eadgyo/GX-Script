package org.eadge.gxscript.test.validator;

import org.eadge.gxscript.classic.entity.displayer.PrintGXEntity;
import org.eadge.gxscript.classic.entity.imbrication.conditionals.IfGXEntity;
import org.eadge.gxscript.classic.entity.types.number.RealGXEntity;
import org.eadge.gxscript.classic.entity.types.number.comparison.EqualToNumberGXEntity;
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

        // Create new GXEntity
        RealGXEntity realEntity = new RealGXEntity();

        // Create start GXEntity
        PrintGXEntity printEntity = new PrintGXEntity("Test");

        // Add direct self interdependency link
        realEntity.linkAsInput(RealGXEntity.SET_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity);
        realEntity.linkAsInput(RealGXEntity.NEXT_INPUT_INDEX, PrintGXEntity.CONTINUE_OUTPUT_INDEX, printEntity);

        // Update starting entities
        script.addEntity(printEntity);
        script.addEntity(realEntity);

        script.updateEntities();

        ValidatorModel validator = new ValidateNoInterdependency();
        return !validator.validate(script);
    }

    public static boolean testInterdependency1()
    {
        // Create entities
        RawGXScript script = new RawGXScript();

        // Create 3 real variables
        RealGXEntity realEntity1 = new RealGXEntity(20f);
        RealGXEntity realEntity2 = new RealGXEntity(10f);
        RealGXEntity realEntity3 = new RealGXEntity();

        // Link realEntity2 on realEntity3 script
        realEntity3.linkAsInput(RealGXEntity.SET_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create real comparison
        EqualToNumberGXEntity equalToNumberEntity = new EqualToNumberGXEntity();
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V0_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity1);
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V1_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create if GXEntity block control
        IfGXEntity ifEntity = new IfGXEntity();
        ifEntity.linkAsInput(IfGXEntity.TEST_INPUT_INDEX, EqualToNumberGXEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        // Create 3 prints for each path
        PrintGXEntity success = new PrintGXEntity("Success");
        success.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        PrintGXEntity fail = new PrintGXEntity("Fail");
        fail.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.FAIL_OUTPUT_INDEX, ifEntity);

        PrintGXEntity continueP = new PrintGXEntity("Continue");
        continueP.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.CONTINUE_OUTPUT_INDEX, ifEntity);

        // Add all created entities to raw gx script
        script.addEntity(realEntity1);
        script.addEntity(realEntity2);
        script.addEntity(realEntity3);
        script.addEntity(equalToNumberEntity);
        script.addEntity(ifEntity);
        script.addEntity(success);
        script.addEntity(fail);
        script.addEntity(continueP);


        // Addd interdepency one imbricated and the first added GXEntity
        realEntity1.linkAsInput(RealGXEntity.NEXT_INPUT_INDEX, PrintGXEntity.CONTINUE_OUTPUT_INDEX, success);

        ValidatorModel validator = new ValidateNoInterdependency();
        return !validator.validate(script);
    }

}
