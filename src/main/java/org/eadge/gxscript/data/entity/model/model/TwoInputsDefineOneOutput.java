package org.eadge.gxscript.data.entity.model.model;

import org.eadge.gxscript.data.entity.model.def.DefaultGXEntity;
import org.eadge.gxscript.data.entity.model.base.GXEntity;

/**
 * Created by eadgyo on 05/09/16.
 *
 * Two inputs define one output
 */
public abstract class TwoInputsDefineOneOutput extends DefaultGXEntity
{
    public static final int S0_INPUT_INDEX   = 0;
    public static final int S1_INPUT_INDEX   = 1;
    public static final int NEXT_INPUT_INDEX = 2;

    public static final int RESULT_OUTPUT_INDEX   = 0;
    public static final int CONTINUE_OUTPUT_INDEX = 1;

    /**
     * Holds the default output class
     */
    private Class defaultClass;

    public TwoInputsDefineOneOutput(String name, Class defaultClass)
    {
        this(name, "S0", "S1", defaultClass);
    }

    public TwoInputsDefineOneOutput(String name, String s0, String s1, Class defaultClass)
    {
        this(name, s0, s1, "Result", defaultClass);
    }

    public TwoInputsDefineOneOutput(String name, String s0, String s1, String result, Class defaultClass)
    {
        super(name);

        addInputEntry(S0_INPUT_INDEX, s0, defaultClass);
        addInputEntry(S1_INPUT_INDEX, s1, defaultClass);
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        addOutputEntry(RESULT_OUTPUT_INDEX, result, defaultClass);
        addOutputEntry(CONTINUE_OUTPUT_INDEX, "Continue", Void.class);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        super.linkAsInput(inputIndex, entityOutput, GXEntity);

        if (inputIndex != NEXT_INPUT_INDEX)
        {
            // Get output class
            Class s0OutputClass  = getOutputClassFromInputEntity(S0_INPUT_INDEX);
            Class s1OutputClass = getOutputClassFromInputEntity(S1_INPUT_INDEX);

            // Get output class
            Class outputClass = getOutputClass(s0OutputClass, s1OutputClass);

            // Change output class
            setOutputClass(RESULT_OUTPUT_INDEX, outputClass);
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        super.unlinkAsInput(inputIndex);

        if (inputIndex != NEXT_INPUT_INDEX)
        {
            // Change output class
            setOutputClass(RESULT_OUTPUT_INDEX, defaultClass);
        }
    }

    /**
     * Get the resulting class from two inputs class
     *
     * @param s0OutputClass first source output class
     * @param s1OutputClass second source output class
     *
     * @return output class updated from sources
     */
    public abstract Class getOutputClass(Class s0OutputClass, Class s1OutputClass);

}