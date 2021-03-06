package org.eadge.gxscript.test;

import org.eadge.gxscript.data.compile.script.CompiledGXScript;
import org.eadge.gxscript.data.compile.script.RawGXScript;
import org.eadge.gxscript.tools.check.ValidatorModel;
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
        gxScript.updateEntities();

        ValidateAllEntitiesPresent validatorAllEntitiesPresent = new ValidateAllEntitiesPresent();

        if (!validatorAllEntitiesPresent.validate(gxScript))
            return false;

        ValidateValidParameters validateValidParameters = new ValidateValidParameters();

        if (!validateValidParameters.validate(gxScript))
            return false;

        ValidatorModel validatorInputs = new ValidateEntityHaveInput();

        if (!validatorInputs.validate(gxScript))
            return false;

        ValidatorModel validatorLinks = new ValidateLinks();

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
