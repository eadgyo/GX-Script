package org.eadge.gxscript.test.validator;

import org.eadge.gxscript.classic.entity.imbrication.conditionals.IfGXEntity;
import org.eadge.gxscript.classic.entity.types.number.RealGXEntity;
import org.eadge.gxscript.data.entity.def.DefaultGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.test.CreateGXScript;
import org.eadge.gxscript.test.PrintTest;
import org.eadge.gxscript.tools.check.validator.ValidateLinks;
import org.eadge.gxscript.tools.check.ValidatorModel;

import java.io.IOException;

/**
 * Created by eadgyo on 11/08/16.
 *
 */
public class TestValidateLinks
{
    public static void main(String[] args) throws IOException
    {
        // Get 3 NotMatchingInputOutputClasses exceptions
        System.out.println("Test validate links");
        PrintTest.printResult(testCorrect(), "Check valid script");
        PrintTest.printResult(testNotCorrectType0(), "Check not valid script, not correct type 0");
        PrintTest.printResult(testNotCorrectType1(), "Check not valid script, not correct type 1");
        PrintTest.printResult(testNotCorrectDerivation0(), "Check not valid script, not correct derivation 0");
    }

    public static boolean testCorrect()
    {
        RawGXScript scriptIf = CreateGXScript.createScriptIf();

        ValidatorModel validator = new ValidateLinks();
        return validator.validate(scriptIf);
    }

    public static boolean testNotCorrectType0()
    {
        RawGXScript script = new RawGXScript();

        // Create entities
        RealGXEntity realEntity = new RealGXEntity();
        script.addEntity(realEntity);

        IfGXEntity ifEntity = new IfGXEntity();
        script.addEntity(ifEntity);

        // Not valid link real to boolean
        linkAsInput(IfGXEntity.TEST_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, ifEntity, realEntity);

        ValidatorModel validator = new ValidateLinks();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectType1()
    {
        RawGXScript script = new RawGXScript();

        // Create entities
        RealGXEntity realEntity = new RealGXEntity();
        script.addEntity(realEntity);

        IfGXEntity ifEntity = new IfGXEntity();
        script.addEntity(ifEntity);

        // Not valid link void to real
        linkAsInput(RealGXEntity.REAL_OUTPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, realEntity, ifEntity);

        ValidatorModel validator = new ValidateLinks();
        return !validator.validate(script);
    }

    public static boolean testNotCorrectDerivation0()
    {
        RawGXScript script = new RawGXScript();

        // Create entities
        DefaultGXEntity objectEntity = new DefaultGXEntity()
        {
            public DefaultGXEntity init()
            {
                // Create one output creating object
                addOutputEntry("Source", Object.class);

                return this;
            }

            @Override
            public Func getFunc()
            {
                return null;
            }
        }.init();
        script.addEntity(objectEntity);

        IfGXEntity ifEntity = new IfGXEntity();
        script.addEntity(ifEntity);

        // Not valid link void to real
        linkAsInput(IfGXEntity.TEST_INPUT_INDEX, 0, ifEntity, objectEntity);

        ValidatorModel validator = new ValidateLinks();
        return !validator.validate(script);
    }

    public static void linkAsInput(int inputIndex, int outputIndex, DefaultGXEntity inputEntity, DefaultGXEntity entityOutput)
    {
        inputEntity.addLinkInput(inputIndex, outputIndex, entityOutput);
        entityOutput.addLinkOutput(outputIndex, inputIndex, inputEntity);
    }
}
