package org.eadge.gxscript.testing.validator;

import org.eadge.gxscript.data.script.CompiledGXScript;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.testing.CreateGXScript;
import org.eadge.gxscript.testing.PrintTest;
import org.eadge.gxscript.tools.check.validator.*;
import org.eadge.gxscript.tools.compile.GXCompiler;
import org.eadge.gxscript.tools.run.GXRunner;

import java.io.IOException;

/**
 * Created by eadgyo on 14/08/16.
 *
 * Test all validator
 */
public class TestAll
{
    public static void main(String[] args) throws IOException
    {
        RawGXScript script = CreateGXScript.createComplexScript2();

        PrintTest.printResult(test(script), "Run script");
    }

    public static boolean test(RawGXScript gxScript)
    {
        gxScript.updateStartingEntities();

        ValidateAllEntitiesPresent validatorAllEntitiesPresent = new ValidateAllEntitiesPresent();

        if (!validatorAllEntitiesPresent.validate(gxScript))
            return false;

        ValidatorModel validatorInputs = new ValidateEntityHaveInput();

        if (!validatorInputs.validate(gxScript))
            return false;

        ValidatorModel validatorLinks = new ValidatorLinks();

        if (!validatorLinks.validate(gxScript))
            return false;

        ValidatorModel validatorNoInterdependency = new ValidateNoInterdependency();

        if (!validatorNoInterdependency.validate(gxScript))
            return false;

        ValidatorModel validatorImbrications = new ValidateImbrication();

        if (!validatorImbrications.validate(gxScript))
            return false;

        GXCompiler gxCompiler = new GXCompiler();
        CompiledGXScript compiledGXScript = gxCompiler.compile(gxScript);

        GXRunner gxRunner = new GXRunner();
        gxRunner.run(compiledGXScript);

        return true;
    }
}
