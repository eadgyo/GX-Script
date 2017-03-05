package org.eadge.gxscript.data.entity.classic.entity.types.collection.list;

import org.eadge.gxscript.data.entity.classic.entity.types.collection.model.CollectionDefineInput;
import org.eadge.gxscript.data.compile.script.func.Func;
import org.eadge.gxscript.data.compile.program.Program;

import java.util.List;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Add one element to the list
 */
public class AddListGXEntity extends CollectionDefineInput
{
    public static final int INDEX_INPUT_INDEX      = 2;
    public static final int NEXT_INPUT_INDEX       = 3;

    public static final int CONTINUE_OUTPUT_INDEX = 0;

    public AddListGXEntity()
    {
        super("Add item in list", "List", List.class);

        // Add script
        addInputEntryNotNeeded(INDEX_INPUT_INDEX, "Index", Integer.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add output
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public Func getFunc()
    {
        if (isInputUsed(INDEX_INPUT_INDEX))
        {
            return new Func()
            {
                @Override
                public void run(Program program)
                {
                    Object[] objects = program.loadCurrentParametersObjects();

                    // Get the list
                    List list = (List) objects[COLLECTION_INPUT_INDEX];

                    // Get the index
                    Integer index = (Integer) objects[INDEX_INPUT_INDEX];

                    // Get the item
                    Object item = objects[ITEM_INPUT_INDEX];

                    // Add the item to the list
                    //noinspection unchecked
                    list.add(index, item);
                }
            };
        }
        else
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
                    Object item = objects[ITEM_INPUT_INDEX];

                    // Add the item to the list
                    //noinspection unchecked
                    list.add(item);
                }
            };
        }
    }
}
