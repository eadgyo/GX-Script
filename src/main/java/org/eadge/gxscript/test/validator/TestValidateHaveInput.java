package org.eadge.gxscript.test.validator;

import org.eadge.gxscript.classic.entity.imbrication.conditionals.IfGXEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.test.PrintTest;
import org.eadge.gxscript.tools.check.validator.ValidateEntityHaveInput;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.io.IOException;

/**
 * Created by eadgyo on 11/08/16.
 *
 */
public class TestValidateHaveInput
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test validate GXEntity have script");
        PrintTest.printResult(testCorrect(), "Check valid script");
        PrintTest.printResult(testNotCorrect(), "Check not valid script");
    }

    public static boolean testCorrect()
    {
        RawGXScript scriptIf = CreateGXScript.createScriptIf();

        ValidatorModel validator = new ValidateEntityHaveInput();
        return validator.validate(scriptIf);
    }

    public static boolean testNotCorrect()
    {
        RawGXScript scriptIf = CreateGXScript.createScriptIf();

        // Create not correct GXEntity with no script
        IfGXEntity ifEntity = new IfGXEntity();
        scriptIf.addEntity(ifEntity);

        ValidatorModel validator = new ValidateEntityHaveInput();
        return !validator.validate(scriptIf);
    }
}
