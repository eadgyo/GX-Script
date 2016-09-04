package org.eadge.gxscript.classic.entity.types.number.operations.maths.models;

import org.eadge.gxscript.data.entity.DefaultEntity;
import org.eadge.gxscript.data.entity.Entity;

/**
 * Created by eadgyo on 04/09/16.
 *
 * Math inverse model
 */
public abstract class MathOneInputModel extends DefaultEntity
{
    public static final int SOURCE_INPUT_ENTITY = 0;
    public static final int NEXT_INPUT_ENTITY = 1;

    public static final int RESULT_INPUT_ENTITY = 0;
    public static final int CONTINUE_INPUT_ENTITY = 1;

    /**
     * Holds the resulting number
     */
    private Class classNumber = Number.class;

    public MathOneInputModel(String operation)
    {
        super(operation);

        addInputEntry(SOURCE_INPUT_ENTITY, "Source", Number.class);
        addInputEntryNotNeeded(NEXT_INPUT_ENTITY, "Next", Void.class);

        addOutputEntry(RESULT_INPUT_ENTITY, "Result", Number.class);
        addOutputEntry(CONTINUE_INPUT_ENTITY, "Continue", Void.class);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, Entity entity)
    {
        super.linkAsInput(inputIndex, entityOutput, entity);

        if (inputIndex != NEXT_INPUT_ENTITY)
        {
            // Get output class
            Class sourceOuputClass = getOutputClassFromInputEntity(SOURCE_INPUT_ENTITY);

            if (sourceOuputClass != null)
            {
                classNumber = sourceOuputClass;
            }
            else
            {
                classNumber = Number.class;
            }
        }
    }

}
