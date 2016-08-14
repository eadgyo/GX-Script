package org.eadge.gxscript.testing;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.entity.displayer.PrintEntity;
import org.eadge.gxscript.data.entity.imbrication.conditionals.IfEntity;
import org.eadge.gxscript.data.entity.imbrication.loops.ForEntity;
import org.eadge.gxscript.data.entity.types.number.IntEntity;
import org.eadge.gxscript.data.entity.types.number.ModifyNumberEntity;
import org.eadge.gxscript.data.entity.types.number.RealEntity;
import org.eadge.gxscript.data.entity.types.number.comparison.EqualToNumberEntity;
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
        RealEntity realEntity1 = new RealEntity(20f);
        RealEntity realEntity2 = new RealEntity(10f);

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
        RealEntity realEntity1 = new RealEntity(20f);
        RealEntity realEntity2 = new RealEntity(10f);
        RealEntity realEntity3 = new RealEntity();

        // Link realEntity2 on realEntity3 input
        realEntity3.linkAsInput(RealEntity.SET_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity2);

        // Create real comparison
        EqualToNumberEntity equalToNumberEntity = new EqualToNumberEntity();
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V0_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity1);
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V1_INPUT_INDEX, RealEntity.REAL_OUTPUT_INDEX, realEntity3);

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

        IntEntity intEntity = new IntEntity(1);
        ForEntity forEntity = new ForEntity();

        forEntity.linkAsInput(ForEntity.ADD_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);

        DefaultEntity isEven = createIsEven();

        isEven.linkAsInput(0, ForEntity.INDEX_FOR_OUTPUT_INDEX, forEntity);
        isEven.linkAsInput(1, ForEntity.DO_INPUT_INDEX, forEntity);

        IfEntity ifEntity = new IfEntity();
        ifEntity.linkAsInput(IfEntity.TEST_INPUT_INDEX, 0, isEven);

        PrintEntity printEntityEven = new PrintEntity("Even");
        PrintEntity printEntityOdd = new PrintEntity("Odd");

        printEntityEven.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);
        printEntityOdd.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.FAIL_OUTPUT_INDEX, ifEntity);

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

        IntEntity intEntity = new IntEntity(1);
        ForEntity forEntity = new ForEntity();

        forEntity.linkAsInput(ForEntity.ADD_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);

        DefaultEntity isEven = createIsEven();

        isEven.linkAsInput(0, ForEntity.INDEX_FOR_OUTPUT_INDEX, forEntity);
        isEven.linkAsInput(1, ForEntity.DO_INPUT_INDEX, forEntity);

        IfEntity ifEntity = new IfEntity();
        ifEntity.linkAsInput(IfEntity.TEST_INPUT_INDEX, 0, isEven);

        PrintEntity printEntityEven = new PrintEntity("Even");
        PrintEntity printEntityOdd = new PrintEntity("Odd");

        printEntityEven.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.SUCCESS_OUTPUT_INDEX, ifEntity);
        printEntityOdd.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, IfEntity.FAIL_OUTPUT_INDEX, ifEntity);

        IntEntity intSetter = new IntEntity(10);
        intSetter.linkAsInput(IntEntity.NEXT_INPUT_INDEX, PrintEntity.CONTINUE_OUTPUT_INDEX, printEntityOdd);

        ModifyingEntity modificationInt = intEntity.createModificationEntity(IntEntity.INT_OUTPUT_INDEX);
        modificationInt.linkAsInput(ModifyNumberEntity.MODIFIED_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);
        modificationInt.linkAsInput(ModifyNumberEntity.SOURCE_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intSetter);

        PrintEntity printChange = new PrintEntity();
        printChange.linkAsInput(PrintEntity.SOURCE_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);
        printChange.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, ModifyNumberEntity.CONTINUE_OUTPUT_INDEX, modificationInt);

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

    private static DefaultEntity createIsEven()
    {
        return new DefaultEntity()
        {
            public final int SOURCE_INPUT_INDEX = 0;
            public final int NEXT_INPUT_INDEX = 1;

            public final int RESULT_OUTPUT_INDEX = 0;

            DefaultEntity init()
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
