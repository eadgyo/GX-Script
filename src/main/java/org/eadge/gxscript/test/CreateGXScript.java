package org.eadge.gxscript.test;

import org.eadge.gxscript.data.compile.program.Program;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.entity.classic.entity.displayer.PrintGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.imbrication.conditionals.IfGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.imbrication.loops.ForGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.types.number.IntGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.types.number.ModifyNumberGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.types.number.RealGXEntity;
import org.eadge.gxscript.data.entity.classic.entity.types.number.comparison.EqualToNumberGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.base.ModifyingGXEntity;
import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.entity.model.script.InputScriptGXEntity;
import org.eadge.gxscript.data.entity.model.script.OutputScriptGXEntity;
import org.eadge.gxscript.data.compile.script.*;
import org.eadge.gxscript.tools.compile.GXCompiler;
import org.eadge.gxscript.tools.compile.GXEntityCreator;

/**
 * Created by eadgyo on 11/08/16.
 *
 */
public class CreateGXScript
{
    public static RawGXScriptDebug createSimpleIf()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create 3 real variables
        RealGXEntity realEntity1 = new RealGXEntity(20f);
        RealGXEntity realEntity2 = new RealGXEntity(20f);

        // Create real comparison
        EqualToNumberGXEntity equalToNumberEntity = new EqualToNumberGXEntity();
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V0_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity1);
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V1_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create if GXEntity block control
        IfGXEntity ifEntity = new IfGXEntity();
        ifEntity.linkAsInput(IfGXEntity.TEST_INPUT_INDEX, EqualToNumberGXEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        // Create 3 prints for each path
        PrintGXEntity success = new PrintGXEntity("EQUAL");
        success.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifEntity);

        PrintGXEntity fail = new PrintGXEntity("DIFF");
        fail.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.FAIL_OUTPUT_INDEX, ifEntity);

        PrintGXEntity continueP = new PrintGXEntity("Continue");
        continueP.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.CONTINUE_OUTPUT_INDEX, ifEntity);

        // Add all created entities to raw gx script
        rawGXScript.addEntity("real1", realEntity1);
        rawGXScript.addEntity("real2", realEntity2);
        rawGXScript.addEntity("equal1", equalToNumberEntity);
        rawGXScript.addEntity("if", ifEntity);
        rawGXScript.addEntity("success", success);
        rawGXScript.addEntity("fail", fail);
        rawGXScript.addEntity("continue", continueP);

        rawGXScript.updateEntities();

        return rawGXScript;
    }

    public static RawGXScriptDebug createScriptIf()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create 3 real variables
        RealGXEntity realEntity1 = new RealGXEntity(20f);
        RealGXEntity realEntity2 = new RealGXEntity(10f);
        RealGXEntity realEntity3 = new RealGXEntity();

        // Link realEntity2 on realEntity3 script
        realEntity3.linkAsInput(RealGXEntity.SET_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create real comparison
        EqualToNumberGXEntity equalToNumberEntity = new EqualToNumberGXEntity();
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V0_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity1);
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V1_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, realEntity3);

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
        rawGXScript.addEntity("real1", realEntity1);
        rawGXScript.addEntity("real2", realEntity2);
        rawGXScript.addEntity("real3", realEntity3);
        rawGXScript.addEntity("equal1", equalToNumberEntity);
        rawGXScript.addEntity("if", ifEntity);
        rawGXScript.addEntity("success", success);
        rawGXScript.addEntity("fail", fail);
        rawGXScript.addEntity("continue", continueP);

        rawGXScript.updateEntities();

        return rawGXScript;
    }

    public static RawGXScriptDebug createComplexScript()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        IntGXEntity intEntity = new IntGXEntity(1);
        ForGXEntity forEntity = new ForGXEntity();

        forEntity.linkAsInput(ForGXEntity.ADD_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);

        DefaultGXEntity isEven = createIsEven();

        isEven.linkAsInput(0, ForGXEntity.INDEX_FOR_OUTPUT_INDEX, forEntity);
        isEven.linkAsInput(1, ForGXEntity.DO_OUTPUT_INDEX, forEntity);

        IfGXEntity ifEntity = new IfGXEntity();
        ifEntity.linkAsInput(IfGXEntity.TEST_INPUT_INDEX, 0, isEven);

        PrintGXEntity printEntityEven = new PrintGXEntity("Even");
        PrintGXEntity printEntityOdd  = new PrintGXEntity("Odd");

        printEntityEven.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifEntity);
        printEntityOdd.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.FAIL_OUTPUT_INDEX, ifEntity);

        rawGXScript.addEntity("int", intEntity);
        rawGXScript.addEntity("for", forEntity);
        rawGXScript.addEntity("isEven", isEven);
        rawGXScript.addEntity("if", ifEntity);
        rawGXScript.addEntity("printEven", printEntityEven);
        rawGXScript.addEntity("printOdd", printEntityOdd);

        rawGXScript.updateEntities();

        return rawGXScript;
    }

    public static RawGXScript createScriptFor()
    {
        RawGXScript rawGXScript = new RawGXScript();
        ForGXEntity forEntity = new ForGXEntity();
        PrintGXEntity printGXEntity = new PrintGXEntity();
        printGXEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, ForGXEntity.DO_OUTPUT_INDEX, forEntity);
        printGXEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, ForGXEntity.INDEX_FOR_OUTPUT_INDEX, forEntity);
        rawGXScript.addEntity(forEntity);
        rawGXScript.addEntity(printGXEntity);

        rawGXScript.updateEntities();
        return rawGXScript;
    }

    public static RawGXScript createComplexScript2()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        IntGXEntity intEntity = new IntGXEntity(1);
        ForGXEntity forEntity = new ForGXEntity();

        forEntity.linkAsInput(ForGXEntity.ADD_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);

        DefaultGXEntity isEven = createIsEven();

        isEven.linkAsInput(0, ForGXEntity.INDEX_FOR_OUTPUT_INDEX, forEntity);
        isEven.linkAsInput(1, ForGXEntity.DO_OUTPUT_INDEX, forEntity);

        IfGXEntity ifEntity = new IfGXEntity();
        ifEntity.linkAsInput(IfGXEntity.TEST_INPUT_INDEX, 0, isEven);

        PrintGXEntity printEntityEven = new PrintGXEntity("Even");
        PrintGXEntity printEntityOdd  = new PrintGXEntity("Odd");

        printEntityEven.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifEntity);
        printEntityOdd.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.FAIL_OUTPUT_INDEX, ifEntity);

        IntGXEntity intSetter = new IntGXEntity(10);
        intSetter.linkAsInput(IntGXEntity.NEXT_INPUT_INDEX, PrintGXEntity.CONTINUE_OUTPUT_INDEX, printEntityOdd);

        ModifyingGXEntity modificationInt = intEntity.createModificationEntity(IntGXEntity.INT_OUTPUT_INDEX);
        modificationInt.linkAsInput(ModifyNumberGXEntity.MODIFIED_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);
        modificationInt.linkAsInput(ModifyNumberGXEntity.SOURCE_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intSetter);

        PrintGXEntity printChange = new PrintGXEntity();
        printChange.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);
        printChange.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, ModifyNumberGXEntity.CONTINUE_OUTPUT_INDEX, modificationInt);

        rawGXScript.addEntity("int", intEntity);
        rawGXScript.addEntity("for", forEntity);
        rawGXScript.addEntity("isEven", isEven);
        rawGXScript.addEntity("if", ifEntity);
        rawGXScript.addEntity("printEven", printEntityEven);
        rawGXScript.addEntity("printOdd", printEntityOdd);
        rawGXScript.addEntity("intSetter", intSetter);
        rawGXScript.addEntity("printChange", printChange);
        rawGXScript.addEntity("modifying", modificationInt);

        rawGXScript.updateEntities();

        return rawGXScript;
    }

    public static RawGXScript createScriptEntityScript()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create 2 inputs variables
        InputScriptGXEntity inputScriptGXEntity1 = new InputScriptGXEntity();
        InputScriptGXEntity inputScriptGXEntity2 = new InputScriptGXEntity();

        // Create real comparison
        EqualToNumberGXEntity equalToNumberEntity = new EqualToNumberGXEntity();
        inputScriptGXEntity1.linkAsOutput(InputScriptGXEntity.SCRIPT_INPUT_OUTPUT, EqualToNumberGXEntity.V0_INPUT_INDEX, equalToNumberEntity);
        inputScriptGXEntity2.linkAsOutput(InputScriptGXEntity.SCRIPT_INPUT_OUTPUT, EqualToNumberGXEntity.V1_INPUT_INDEX, equalToNumberEntity);

        // Create if GXEntity block control
        IfGXEntity ifEntity = new IfGXEntity();
        ifEntity.linkAsInput(IfGXEntity.TEST_INPUT_INDEX, EqualToNumberGXEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        RealGXEntity outputReal = new RealGXEntity(0);
        // Create 3 prints for each path
        PrintGXEntity success = new PrintGXEntity("EQUAL");
        success.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.SUCCESS_OUTPUT_INDEX, ifEntity);
        RealGXEntity successReal = new RealGXEntity(1);
        ModifyNumberGXEntity modifySuccess = new ModifyNumberGXEntity();
        modifySuccess.linkAsInput(ModifyNumberGXEntity.MODIFIED_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, outputReal);
        modifySuccess.linkAsInput(ModifyNumberGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, successReal);


        PrintGXEntity fail = new PrintGXEntity("DIFF");
        fail.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.FAIL_OUTPUT_INDEX, ifEntity);
        RealGXEntity failReal = new RealGXEntity(2);
        ModifyNumberGXEntity modifyFail = new ModifyNumberGXEntity();
        modifyFail.linkAsInput(ModifyNumberGXEntity.MODIFIED_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, outputReal);
        modifyFail.linkAsInput(ModifyNumberGXEntity.SOURCE_INPUT_INDEX, RealGXEntity.REAL_OUTPUT_INDEX, failReal);


        // Create output entities
        OutputScriptGXEntity outputScriptGXEntity = new OutputScriptGXEntity();
        outputScriptGXEntity.linkAsInput(OutputScriptGXEntity.SCRIPT_OUTPUT_INPUT, RealGXEntity.REAL_OUTPUT_INDEX, outputReal);

        PrintGXEntity continueP = new PrintGXEntity("Continue");
        continueP.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, IfGXEntity.CONTINUE_OUTPUT_INDEX, ifEntity);

        // Add all created entities to raw gx script
        rawGXScript.addEntity("input1", inputScriptGXEntity1);
        rawGXScript.addEntity("input2", inputScriptGXEntity2);
        rawGXScript.addEntity("equal1", equalToNumberEntity);
        rawGXScript.addEntity("if", ifEntity);
        rawGXScript.addEntity("success", success);
        rawGXScript.addEntity("fail", fail);
        rawGXScript.addEntity("successReal", successReal);
        rawGXScript.addEntity("failReal", failReal);
        rawGXScript.addEntity("successModify", modifySuccess);
        rawGXScript.addEntity("fail", modifyFail);
        rawGXScript.addEntity("continue", continueP);
        rawGXScript.addEntity("outputReal", outputReal);
        rawGXScript.addEntity("output", outputScriptGXEntity);

        rawGXScript.updateEntities();

        return rawGXScript;

    }

    public static RawGXScript createFullScriptWithCompiledScript()
    {
        RawGXScript scriptEntityScript = createScriptEntityScript();
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Compile this script:
        GXCompiler compiler = new GXCompiler();
        CompiledGXScript compiledGXScript = compiler.compile(scriptEntityScript);

        // Create entity from compiled script
        GXEntityCreator gxEntityCreator = new GXEntityCreator();
        GXEntity        gxEntity        = gxEntityCreator.createGXEntity(compiledGXScript);

        // Create 2 reals
        RealGXEntity realGXEntity1 = new RealGXEntity(20);
        RealGXEntity realGXEntity2  = new RealGXEntity(21);

        gxEntity.linkAsInput(0, RealGXEntity.REAL_OUTPUT_INDEX, realGXEntity1);
        gxEntity.linkAsInput(1, RealGXEntity.REAL_OUTPUT_INDEX, realGXEntity2);

        // Create print result
        PrintGXEntity printGXEntity = new PrintGXEntity();
        printGXEntity.linkAsInput(PrintGXEntity.SOURCE_INPUT_INDEX, 0, gxEntity);


        rawGXScript.addEntity("Real1", realGXEntity1);
        rawGXScript.addEntity("Real2", realGXEntity2);
        rawGXScript.addEntity("Script", gxEntity);
        rawGXScript.addEntity("Print", printGXEntity);

        rawGXScript.updateEntities();

        return rawGXScript;
    }

    private static DefaultGXEntity createIsEven()
    {
        return new DefaultGXEntity()
        {
            public final int SOURCE_INPUT_INDEX = 0;
            public final int NEXT_INPUT_INDEX = 1;

            public final int RESULT_OUTPUT_INDEX = 0;

            DefaultGXEntity init()
            {
                setName("IsEven");

                addInputEntry(SOURCE_INPUT_INDEX, "Source", Integer.class);
                addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

                addOutputEntry(RESULT_OUTPUT_INDEX, "Result", Boolean.class);

                return this;
            }

            @Override
            public Func getFunc()
            {
                return new Func()
                {
                    @Override
                    public void run(Program program)
                    {
                        Object[] objects = program.loadCurrentParametersObjects();

                        Integer source = (Integer) objects[0];

                        // Push is source is an odd number
                        program.pushInMemory(source%2 == 0);
                    }
                };
            }
        }.init();
    }
}
