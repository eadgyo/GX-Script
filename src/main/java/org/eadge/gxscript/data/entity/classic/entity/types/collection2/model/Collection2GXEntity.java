package org.eadge.gxscript.data.entity.classic.entity.types.collection2.model;

import org.eadge.gxscript.data.entity.classic.entity.types.collection2.Class2Items;
import org.eadge.gxscript.data.entity.model.def.DefaultVariableGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Model for collection with 2 entries
 */
public abstract class Collection2GXEntity extends DefaultVariableGXEntity implements Class2Items
{
    public static final int SOURCE_INPUT_INDEX   = 0;
    public static final int E0_CLASS_INPUT_INDEX = 1;
    public static final int E1_CLASS_INPUT_INDEX = 2;
    public static final int NEXT_INPUT_INDEX     = 3;

    public static final int COLLECTION_OUTPUT_INDEX = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    protected Class item0Class;
    protected Class item1Class;

    private Class defaultClass;

    public Collection2GXEntity(String name, Class collectionClass, Class defaultClass)
    {
        super(name);

        this.defaultClass = defaultClass;

        this.item0Class = defaultClass;
        this.item1Class = defaultClass;

        // Add inputs
        addInputEntryNotNeeded(SOURCE_INPUT_INDEX, "Source", collectionClass);
        addInputEntryNotNeeded(E0_CLASS_INPUT_INDEX, "Entry 0 Class", defaultClass);
        addInputEntryNotNeeded(E1_CLASS_INPUT_INDEX, "Entry 1 Class", defaultClass);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Add outputs
        addOutputEntry(COLLECTION_OUTPUT_INDEX, "Collection2GXEntity", collectionClass);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex == SOURCE_INPUT_INDEX)
        {
            if (isInputUsed(E0_CLASS_INPUT_INDEX))
            {
                unlinkAsInput(E0_CLASS_INPUT_INDEX);
            }

            if (isInputUsed(E1_CLASS_INPUT_INDEX))
            {
                unlinkAsInput(E1_CLASS_INPUT_INDEX);
            }

            // Get output class
            item0Class = ((Class2Items) getInputEntity(SOURCE_INPUT_INDEX)).getItem0Class();
            item1Class = ((Class2Items) getInputEntity(SOURCE_INPUT_INDEX)).getItem1Class();
        }
        else if (inputIndex == E0_CLASS_INPUT_INDEX || inputIndex == E1_CLASS_INPUT_INDEX)
        {
            if (isInputUsed(SOURCE_INPUT_INDEX))
            {
                unlinkAsInput(SOURCE_INPUT_INDEX);
            }

            // Get output class
            if (isInputUsed(E0_CLASS_INPUT_INDEX))
            {
                item0Class = ((Class2Items) getInputEntity(SOURCE_INPUT_INDEX)).getItem0Class();
            }
            else
            {
                item0Class = defaultClass;
            }

            if (isInputUsed(E1_CLASS_INPUT_INDEX))
            {
                item1Class = ((Class2Items) getInputEntity(SOURCE_INPUT_INDEX)).getItem1Class();
            }
            else
            {
                item1Class = defaultClass;
            }
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex == SOURCE_INPUT_INDEX)
        {
            // Change output class
            item0Class = defaultClass;
            item1Class = defaultClass;
        }

        if (inputIndex == E0_CLASS_INPUT_INDEX)
        {
            item0Class = defaultClass;
        }

        if (inputIndex == E1_CLASS_INPUT_INDEX)
        {
            item1Class = defaultClass;
        }
    }

    public Class getItem0Class()
    {
        return item0Class;
    }

    public Class getItem1Class()
    {
        return item1Class;
    }
}
