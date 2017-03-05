package org.eadge.gxscript.data.entity.classic.entity.types.collection.model;

import org.eadge.gxscript.data.entity.classic.entity.types.collection.ClassItem;
import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;

/**
 * Created by eadgyo on 10/09/16.
 *
 * Collection in script define one script class
 */
public abstract class CollectionDefineInput extends DefaultGXEntity
{
    public static final int COLLECTION_INPUT_INDEX = 0;
    public static final int ITEM_INPUT_INDEX       = 1;

    public CollectionDefineInput(String name, String collectionName, Class collectionClass)
    {
        super(name);

        addInputEntry(COLLECTION_INPUT_INDEX, collectionName, collectionClass);
        addInputEntry(ITEM_INPUT_INDEX, "Item", DEFAULT_CLASS);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex == COLLECTION_INPUT_INDEX)
        {
            // Get output class
            Class itemClass = ((ClassItem) getInputEntity(COLLECTION_INPUT_INDEX)).getItemClass();

            // Change script item class
            setInputClass(ITEM_INPUT_INDEX, itemClass);
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex == COLLECTION_INPUT_INDEX)
        {
            // Change output class
            setInputClass(ITEM_INPUT_INDEX, DEFAULT_CLASS);
        }
    }
}
