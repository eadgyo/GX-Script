package org.eadge.gxscript.data.entity.classic.entity.types.collection;

import org.eadge.gxscript.data.entity.model.def.DefaultVariableGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Collection GXEntity
 */
public abstract class CollectionGXEntity extends DefaultVariableGXEntity implements ClassItem
{
    public static final int SOURCE_INPUT_INDEX = 0;
    public static final int CLASS_INPUT_INDEX = 1;
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int COLLECTION_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX   = 1;

    protected Class itemClass;
    private Class defaultClass;

    public CollectionGXEntity(String name, Class collectionClass, Class defaultClass)
    {
        super(name);

        this.defaultClass = defaultClass;
        itemClass = defaultClass;

        // Add inputs
        addInputEntryNotNeeded(SOURCE_INPUT_INDEX, "Source", Collection.class);
        addInputEntryNotNeeded(CLASS_INPUT_INDEX, "Class", Object.class);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add outputs
        addOutputEntry(COLLECTION_OUTPUT_INDEX, "Collection", collectionClass);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);

    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex == SOURCE_INPUT_INDEX)
        {
            if (isInputUsed(CLASS_INPUT_INDEX))
            {
                unlinkAsInput(CLASS_INPUT_INDEX);
            }

            // Get output class
            itemClass = ((ClassItem) getInputEntity(SOURCE_INPUT_INDEX)).getItemClass();
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
            itemClass = defaultClass;
        }
    }

    public Class getItemClass()
    {
        return itemClass;
    }

    public void setItemClass(Class itemClass)
    {
        this.itemClass = itemClass;
    }
}
