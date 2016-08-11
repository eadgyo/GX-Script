package org.eadge.gxscript.testing.validator;

import org.eadge.gxscript.data.entity.imbrication.conditionals.IfEntity;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.testing.CreateGXScript;
import org.eadge.gxscript.testing.PrintTest;
import org.eadge.gxscript.tools.check.validator.ValidateEntityHaveInput;
import org.eadge.gxscript.tools.check.validator.ValidatorModel;

import java.io.IOException;

/**
 * Created by eadgyo on 11/08/16.
 *
 */
public class TestValidateHaveInput
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test validate entity have input");
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

        // Create not correct entity with no input
        IfEntity ifEntity = new IfEntity();
        scriptIf.addEntity(ifEntity);

        ValidatorModel validator = new ValidateEntityHaveInput();
        return validator.validate(scriptIf);
    }
}
