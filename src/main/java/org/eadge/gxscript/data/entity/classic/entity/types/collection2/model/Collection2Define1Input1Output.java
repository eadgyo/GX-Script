package org.eadge.gxscript.data.entity.classic.entity.types.collection2.model;

import org.eadge.gxscript.data.entity.classic.entity.types.collection2.Class2Items;
import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;

/**
 * Created by eadgyo on 11/09/16.
 *
 * Collection2 define one script and one output
 */
public abstract class Collection2Define1Input1Output extends DefaultGXEntity
{
    public static final int COLLECTION2_INPUT_INDEX = 0;
    public static final int E_INPUT_INDEX           = 1;
    public static final int E_OUTPUT_INDEX          = 0;

    public Collection2Define1Input1Output(String name, String collectionName, Class collection2Class)
    {
        this(name, collectionName, collection2Class, "Entry 0", "Entry 1");
    }

    public Collection2Define1Input1Output(String name, String collectionName, Class collection2Class, String inputName, String outputName)
    {
        super(name);

        addInputEntry(COLLECTION2_INPUT_INDEX, collectionName, collection2Class);
        addInputEntry(E_INPUT_INDEX, inputName, DEFAULT_CLASS);

        addOutputEntry(E_OUTPUT_INDEX, outputName, DEFAULT_CLASS);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex == COLLECTION2_INPUT_INDEX)
        {
            // Get output class
            Class itemClass0 = ((Class2Items) getInputEntity(COLLECTION2_INPUT_INDEX)).getItem0Class();
            Class itemClass1 = ((Class2Items) getInputEntity(COLLECTION2_INPUT_INDEX)).getItem1Class();

            // Change script and output class
            setInputClass(E_INPUT_INDEX, itemClass0);
            setOutputClass(E_OUTPUT_INDEX, itemClass1);
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex == COLLECTION2_INPUT_INDEX)
        {
            // Change script and output class
            setInputClass(E_INPUT_INDEX, DEFAULT_CLASS);
            setOutputClass(E_OUTPUT_INDEX, DEFAULT_CLASS);
        }
    }
}