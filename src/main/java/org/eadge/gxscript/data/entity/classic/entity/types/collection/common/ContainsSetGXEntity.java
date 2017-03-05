package org.eadge.gxscript.data.entity.classic.entity.types.collection.common;

import org.eadge.gxscript.data.entity.classic.entity.types.collection.model.CollectionDefineInput;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Check if one item is contained in collection
 */
public class ContainsSetGXEntity extends CollectionDefineInput
{
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int RESULT_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public ContainsSetGXEntity()
    {
        super("Item contained in collection", "Collection", Collection.class);

        // Inputs
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Outputs
        addOutputEntry(RESULT_OUTPUT_INDEX, "Result", Boolean.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
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

                // Get collection
                Collection collection = (Collection) objects[COLLECTION_INPUT_INDEX];

                // Get item
                Object item = objects[ITEM_INPUT_INDEX];

                // Check if the item is in the collection
                Boolean result = collection.contains(item);

                // Push result in memory
                program.pushInMemory(result);
            }
        };
    }
}
