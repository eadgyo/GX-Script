package org.eadge.gxscript.classic.entity.types.collection;

import org.eadge.gxscript.data.entity.DefaultVariableEntity;
import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;
import java.util.List;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Collection entity
 */
public abstract class CollectionEntity extends DefaultVariableEntity
{
    public static final int SOURCE_INPUT_INDEX = 0;
    public static final int CLASS_INPUT_INDEX = 1;
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int COLLECTION_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX   = 1;

    protected Class itemClass;

    public CollectionEntity(String name, Class defaultClass)
    {
        super(name);

        itemClass = defaultClass;

        // Add input
        addInputEntryNotNeeded(SOURCE_INPUT_INDEX, "Source", Collection.class);
        addInputEntryNotNeeded(CLASS_INPUT_INDEX, "Class", Object.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add output
        addOutputEntry(COLLECTION_OUTPUT_INDEX, "List", List.class);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);

    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, Entity entity)
    {
        super.linkAsInput(inputIndex, entityOutput, entity);

        if (inputIndex == SOURCE_INPUT_INDEX)
        {
            if (isInputUsed(CLASS_INPUT_INDEX))
            {
                unlinkAsInput(CLASS_INPUT_INDEX);
            }

            // Get output class
            itemClass = ((CollectionEntity) getInputEntity(SOURCE_INPUT_INDEX)).getItemClass();
        }
        else if (inputIndex == CLASS_INPUT_INDEX)
        {
            if (isInputUsed(SOURCE_INPUT_INDEX))
            {
                unlinkAsInput(SOURCE_INPUT_INDEX);
            }

            // Get output class
            itemClass = getOutputClassFromInputEntity(SOURCE_INPUT_INDEX);
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex == SOURCE_INPUT_INDEX || inputIndex == CLASS_INPUT_INDEX)
        {
            // Change output class
            itemClass = null;
        }
    }

    public abstract Class getItemClass();
}
