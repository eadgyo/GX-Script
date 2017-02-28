package org.eadge.gxscript.classic.test.imbrication;

import org.eadge.gxscript.classic.entity.displayer.PrintGXEntity;
import org.eadge.gxscript.classic.entity.imbrication.loops.WhileGXEntity;
import org.eadge.gxscript.classic.entity.types.bool.BoolGXEntity;
import org.eadge.gxscript.classic.entity.types.bool.ModifyBoolGXEntity;
import org.eadge.gxscript.classic.entity.types.bool.transform.InvertBoolGXEntity;
import org.eadge.gxscript.classic.entity.types.number.IntGXEntity;
import org.eadge.gxscript.classic.entity.types.number.comparison.EqualToNumberGXEntity;
import org.eadge.gxscript.classic.entity.types.number.operations.maths.AddNumbersGXEntity;
import org.eadge.gxscript.data.entity.ModifyingGXEntity;
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
        IntGXEntity intEntity  = new IntGXEntity(2);
        IntGXEntity intEntity1 = new IntGXEntity(0);

        IntGXEntity addEntity = new IntGXEntity(-1);

        // Create while GXEntity
        WhileGXEntity whileEntity = new WhileGXEntity();

        // Create test block
        EqualToNumberGXEntity equalToNumberEntity  = new EqualToNumberGXEntity();
        EqualToNumberGXEntity equalToNumberEntity1 = new EqualToNumberGXEntity();

        // Invert result
        InvertBoolGXEntity invertBoolEntity  = new InvertBoolGXEntity();
        InvertBoolGXEntity invertBoolEntity1 = new InvertBoolGXEntity();

        // Save the result
        BoolGXEntity       boolEntity       = new BoolGXEntity();
        ModifyBoolGXEntity modifyBoolEntity = new ModifyBoolGXEntity();

        // Create modifiying elements
        ModifyingGXEntity modificationEntity = intEntity.createModificationEntity(IntGXEntity.INT_OUTPUT_INDEX);

        // Create print
        PrintGXEntity printEntity = new PrintGXEntity("I'm here 2 times");

        // Test add GXEntity
        AddNumbersGXEntity addNumbersEntity = new AddNumbersGXEntity();

        // Create links
        // Link entities to equals comparison
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V0_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);
        equalToNumberEntity.linkAsInput(EqualToNumberGXEntity.V1_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity1);

        // Invert the result
        invertBoolEntity.linkAsInput(InvertBoolGXEntity.SOURCE_INPUT_INDEX, EqualToNumberGXEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity);

        // Save the result
        boolEntity.linkAsInput(BoolGXEntity.SET_INPUT_INDEX, InvertBoolGXEntity.INVERTED_OUTPUT_INDEX, invertBoolEntity);

        // Link result to while
        whileEntity.linkAsInput(WhileGXEntity.TEST_INPUT_INDEX, BoolGXEntity.BOOL_OUTPUT_INDEX, boolEntity);

        // WHILE PROCESS
        // Link print to while do
        printEntity.linkAsInput(PrintGXEntity.NEXT_INPUT_INDEX, WhileGXEntity.DO_OUTPUT_INDEX, whileEntity);

        // Then remove one from the int 0
        addNumbersEntity.linkAsInput(AddNumbersGXEntity.S0_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);
        addNumbersEntity.linkAsInput(AddNumbersGXEntity.S1_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, addEntity);
        addNumbersEntity.linkAsInput(AddNumbersGXEntity.NEXT_INPUT_INDEX, PrintGXEntity.CONTINUE_OUTPUT_INDEX, printEntity);

        // Modify int
        modificationEntity.linkAsInput(0, IntGXEntity.INT_OUTPUT_INDEX, intEntity);
        modificationEntity.linkAsInput(1, AddNumbersGXEntity.RESULT_OUTPUT_INDEX, addNumbersEntity);

        // Test conditions
        equalToNumberEntity1.linkAsInput(EqualToNumberGXEntity.V0_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity);
        equalToNumberEntity1.linkAsInput(EqualToNumberGXEntity.V1_INPUT_INDEX, IntGXEntity.INT_OUTPUT_INDEX, intEntity1);
        equalToNumberEntity1.linkAsInput(EqualToNumberGXEntity.NEXT_INPUT_INDEX, 0, modificationEntity);

        // Invert the result
        invertBoolEntity1.linkAsInput(InvertBoolGXEntity.SOURCE_INPUT_INDEX, EqualToNumberGXEntity.RESULT_OUTPUT_INDEX, equalToNumberEntity1);

        // Modify the boolean
        modifyBoolEntity.linkAsInput(ModifyBoolGXEntity.MODIFIED_INPUT_INDEX, BoolGXEntity.BOOL_OUTPUT_INDEX, boolEntity);
        modifyBoolEntity.linkAsInput(ModifyBoolGXEntity.SOURCE_INPUT_INDEX, InvertBoolGXEntity.INVERTED_OUTPUT_INDEX, invertBoolEntity1);

        rawGXScript.addEntityRecursiveSearch(intEntity);
        return RunTest.run(rawGXScript);
    }
}
