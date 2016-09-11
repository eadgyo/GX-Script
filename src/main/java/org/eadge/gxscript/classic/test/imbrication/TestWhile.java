package org.eadge.gxscript.classic.test.imbrication;

import org.eadge.gxscript.classic.entity.displayer.PrintEntity;
import org.eadge.gxscript.classic.entity.imbrication.loops.WhileEntity;
import org.eadge.gxscript.classic.entity.types.bool.BoolEntity;
import org.eadge.gxscript.classic.entity.types.bool.ModifyBoolEntity;
import org.eadge.gxscript.classic.entity.types.bool.transform.InvertBoolEntity;
import org.eadge.gxscript.classic.entity.types.number.IntEntity;
import org.eadge.gxscript.classic.entity.types.number.comparison.EqualToNumberEntity;
import org.eadge.gxscript.classic.entity.types.number.operations.maths.AddNumbersEntity;
import org.eadge.gxscript.data.entity.ModifyingEntity;
import org.eadge.gxscript.data.script.RawGXScriptDebug;
import org.eadge.gxscript.test.PrintTest;

import java.io.IOException;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Test while
 */
public class TestWhile
{
    public static void main(String[] args) throws IOException
    {
        System.out.println("Test while");

        PrintTest.printResult(test(), "Check valid script");;
    }

    public static boolean test()
    {
        RawGXScriptDebug rawGXScript = new RawGXScriptDebug();

        // Create blocks
        // Create the integer to be used with while
        IntEntity intEntity = new IntEntity(2);
        IntEntity intEntity1 = new IntEntity(0);

        IntEntity addEntity = new IntEntity(-1);

        // Create while entity
        WhileEntity whileEntity = new WhileEntity();

        // Create test block
        EqualToNumberEntity equalToNumberEntity = new EqualToNumberEntity();
        EqualToNumberEntity equalToNumberEntity1 = new EqualToNumberEntity();

        // Invert result
        InvertBoolEntity invertBoolEntity = new InvertBoolEntity();
        InvertBoolEntity invertBoolEntity1 = new InvertBoolEntity();

        // Save the result
        BoolEntity boolEntity = new BoolEntity();
        ModifyBoolEntity modifyBoolEntity = new ModifyBoolEntity();

        // Create modifiying elements
        ModifyingEntity modificationEntity = intEntity.createModificationEntity(IntEntity.INT_OUTPUT_INDEX);

        // Create print
        PrintEntity printEntity = new PrintEntity("I'm here 2 times");

        // Test add entity
        AddNumbersEntity addNumbersEntity = new AddNumbersEntity();

        // Create links
        // Link entities to equals comparison
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V0_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);
        equalToNumberEntity.linkAsInput(EqualToNumberEntity.V1_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity1);

        // Invert the result
        invertBoolEntity.linkAsInput(InvertBoolEntity.SOURCE_INPUT_INDEX, EqualToNumberEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        // Save the result
        boolEntity.linkAsInput(BoolEntity.SET_INPUT_INDEX, InvertBoolEntity.INVERTED_OUTPUT_INDEX, invertBoolEntity);

        // Link result to while
        whileEntity.linkAsInput(WhileEntity.TEST_INPUT_INDEX, BoolEntity.BOOL_OUTPUT_INDEX, boolEntity);

        // WHILE PROCESS
        // Link print to while do
        printEntity.linkAsInput(PrintEntity.NEXT_INPUT_INDEX, WhileEntity.DO_OUTPUT_INDEX, whileEntity);

        // Then remove one from the int 0
        addNumbersEntity.linkAsInput(AddNumbersEntity.S0_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);
        addNumbersEntity.linkAsInput(AddNumbersEntity.S1_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, addEntity);
        addNumbersEntity.linkAsInput(AddNumbersEntity.NEXT_INPUT_INDEX, PrintEntity.CONTINUE_OUTPUT_INDEX, printEntity);

        // Modify int
        modificationEntity.linkAsInput(0, IntEntity.INT_OUTPUT_INDEX, intEntity);
        modificationEntity.linkAsInput(1, AddNumbersEntity.RESULT_OUTPUT_INDEX, addNumbersEntity);

        // Test conditions
        equalToNumberEntity1.linkAsInput(EqualToNumberEntity.V0_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity);
        equalToNumberEntity1.linkAsInput(EqualToNumberEntity.V1_INPUT_INDEX, IntEntity.INT_OUTPUT_INDEX, intEntity1);
        equalToNumberEntity1.linkAsInput(EqualToNumberEntity.NEXT_INPUT_INDEX, 0, modificationEntity);

        // Invert the result
        invertBoolEntity1.linkAsInput(InvertBoolEntity.SOURCE_INPUT_INDEX, EqualToNumberEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity1);

        // Modify the boolean
        modifyBoolEntity.linkAsInput(ModifyBoolEntity.MODIFIED_INPUT_INDEX, BoolEntity.BOOL_OUTPUT_INDEX, boolEntity);
        modifyBoolEntity.linkAsInput(ModifyBoolEntity.SOURCE_INPUT_INDEX, InvertBoolEntity.INVERTED_OUTPUT_INDEX, invertBoolEntity1);

        rawGXScript.addEntityRecursiveSearch(intEntity);
        return RunTest.run(rawGXScript);
    }
}
