package org.eadge.gxscript.classic.entity.types.collection.set;

import org.eadge.gxscript.classic.entity.types.collection.model.CollectionDefineInput;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Remove one item from set
 */
public class RemoveSetGXEntity extends CollectionDefineInput
{
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int CONTINUE_OUTPUT_INDEX = 0;

    public RemoveSetGXEntity()
    {
        super("Remove item from set", "Set", Set.class);

        // Add script
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add output
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

                // Get the set
                Set set = (Set) objects[COLLECTION_INPUT_INDEX];

                // Get the item
                Object item = objects[ITEM_INPUT_INDEX];

                // Add the item to the list
                //noinspection unchecked
                set.remove(item);
            }
        };
    }

}
