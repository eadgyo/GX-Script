package org.eadge.gxscript.classic.entity.types.collection2.model;

import org.eadge.gxscript.classic.entity.types.collection2.Class2Items;
import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.entity.Entity;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Collection2 define 2 inputs
 */
public abstract class Collection2Define2Inputs extends DefaultEntity
{
    public static final int COLLECTION2_INPUT_INDEX = 0;
    public static final int E0_INPUT_INDEX          = 1;
    public static final int E1_INPUT_INDEX          = 1;

    public Collection2Define2Inputs(String name, String collectionName, Class collection2Class)
    {
        super(name);

        addInputEntry(COLLECTION2_INPUT_INDEX, collectionName, collection2Class);
        addInputEntry(E0_INPUT_INDEX, "Entry 0", DEFAULT_CLASS);
        addInputEntry(E1_INPUT_INDEX, "Entry 1", DEFAULT_CLASS);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, Entity entity)
    {
        super.linkAsInput(inputIndex, entityOutput, entity);

        if (inputIndex == COLLECTION2_INPUT_INDEX)
        {
            // Get output class
            Class itemClass0 = ((Class2Items) getInputEntity(COLLECTION2_INPUT_INDEX)).getItem0Class();
            Class itemClass1 = ((Class2Items) getInputEntity(COLLECTION2_INPUT_INDEX)).getItem1Class();

            // Change input item class
            setInputClass(E0_INPUT_INDEX, itemClass0);
            setInputClass(E1_INPUT_INDEX, itemClass1);
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex == COLLECTION2_INPUT_INDEX)
        {
            // Change output class
            setInputClass(E0_INPUT_INDEX, DEFAULT_CLASS);
            setInputClass(E1_INPUT_INDEX, DEFAULT_CLASS);
        }
    }
}