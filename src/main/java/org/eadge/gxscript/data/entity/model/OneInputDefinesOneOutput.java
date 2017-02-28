package org.eadge.gxscript.data.entity.model;

import org.eadge.gxscript.data.entity.DefaultGXEntity;
import org.eadge.gxscript.data.entity.GXEntity;

/**
 * Created by eadgyo on 05/09/16.
 *
 * One function object define the output class object
 */
public abstract class OneInputDefinesOneOutput extends DefaultGXEntity
{
    public static final int SOURCE_INPUT_INDEX = 0;
    public static final int NEXT_INPUT_INDEX   = 1;

    public static final int RESULT_INPUT_INDEX   = 0;
    public static final int CONTINUE_INPUT_INDEX = 1;

    /**
     * Holds default class
     */
    protected Class defaultClass;

    public OneInputDefinesOneOutput(String name, Class defaultClass)
    {
        this(name, "Result", defaultClass);
    }

    public OneInputDefinesOneOutput(String name, String resultName, Class defaultClass)
    {
        super(name);

        this.defaultClass = defaultClass;

        addInputEntry(SOURCE_INPUT_INDEX, "Source", defaultClass);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(RESULT_INPUT_INDEX, resultName, defaultClass);
        addOutputEntry(CONTINUE_INPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex != NEXT_INPUT_INDEX)
        {
            // Get output class
            Class sourceOutputClass = getOutputClassFromInputEntity(SOURCE_INPUT_INDEX);

            // If there is a linked function GXEntity
            if (sourceOutputClass != null)
            {
                setOutputClass(RESULT_INPUT_INDEX, sourceOutputClass);
            }
            else
            {
                setOutputClass(RESULT_INPUT_INDEX, defaultClass);
            }
        }
    }


    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex != NEXT_INPUT_INDEX)
        {
            // Change output class
            setOutputClass(RESULT_INPUT_INDEX, defaultClass);
        }
    }
}
