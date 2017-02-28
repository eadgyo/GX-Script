package org.eadge.gxscript.classic.entity.types.collection.list;

import org.eadge.gxscript.classic.entity.types.collection.model.CollectionDefineOutput;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;

import java.util.List;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Remove one item from list using index
 */
public class RemoveListGXEntity extends CollectionDefineOutput
{
    public static final int INDEX_INPUT_INDEX = 1;
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int CONTINUE_OUTPUT_INDEX = 1;

    public RemoveListGXEntity()
    {
        super("Remove item from list", "List", List.class);

        // Add function
        addInputEntry(INDEX_INPUT_INDEX, "Item index", Integer.class);
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

                // Get the list
                List list = (List) objects[COLLECTION_INPUT_INDEX];

                // Get the item
                Object item = list.remove(INDEX_INPUT_INDEX);

                // Push in memory item
                program.pushInMemory(item);
            }
        };
    }

}
