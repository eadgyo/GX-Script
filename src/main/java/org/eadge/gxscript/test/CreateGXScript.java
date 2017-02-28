package org.eadge.gxscript.test;

import org.eadge.gxscript.classic.entity.displayer.PrintGXEntity;
import org.eadge.gxscript.classic.entity.imbrication.conditionals.IfGXEntity;
import org.eadge.gxscript.classic.entity.imbrication.loops.ForGXEntity;
import org.eadge.gxscript.classic.entity.types.number.IntGXEntity;
import org.eadge.gxscript.classic.entity.types.number.RealGXEntity;
import org.eadge.gxscript.classic.entity.types.number.comparison.EqualToNumberGXEntity;
import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.entity.ModifyingGXEntity;
import org.eadge.gxscript.classic.entity.types.number.ModifyNumberGXEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.RawGXScript;
import org.eadge.gxscript.data.script.RawGXScriptDebug;

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

        rawGXScript.updateStartingEntities();

        return rawGXScript;
    }

    public static RawGXScriptDebug createScriptIf()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create 3 real variables
        RealGXEntity realEntity1 = new RealGXEntity(20f);
        RealGXEntity realEntity2 = new RealGXEntity(10f);
        RealGXEntity realEntity3 = new RealGXEntity();

        // Link realEntity2 on realEntity3 function
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

        rawGXScript.updateStartingEntities();

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

        rawGXScript.updateStartingEntities();

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

        rawGXScript.updateStartingEntities();

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
