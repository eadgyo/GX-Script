package org.eadge.gxscript.classic.entity.imbrication.conditionals;

import org.eadge.gxscript.data.entity.DefaultStartImbricationEntity;
import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.Program;
import org.eadge.gxscript.data.script.address.FuncAddress;
import org.eadge.gxscript.data.script.address.FuncImbricationDataAddresses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Switch entity block
 * Always let one entry unoccupied to let the user add new case
 */
public class SwitchEntity extends DefaultStartImbricationEntity
{
    public static final int SOURCE_INPUT_INDEX = 0;
    public static final int NEXT_INPUT_INDEX = 1;

    /**
     * Continue output index can move
     */
    private int continueOutputIndex;
    private int defaultOutputIndex;

    private ArrayList<Integer> casesValue = new ArrayList<>();

    public SwitchEntity()
    {
        super("Switch");
        // Input
        // Add source index
        addInputEntry(SOURCE_INPUT_INDEX, "Source", Integer.class);

        // Add next function
        addInputEntryNotNeeded(NEXT_INPUT_INDEX, "Next", Void.class);

        // Output
        // Set output indices
        defaultOutputIndex = 0;
        continueOutputIndex = 0;

        // Add the default output
        addOutputImbricatedEntry(defaultOutputIndex, 0, "Default", Void.class);
        defaultOutputIndex = 0;

        // Add continue output
        addOutputEntry(continueOutputIndex, "Continue", Void.class);
        continueOutputIndex = 1;

        // Add the first case index
        addOutputCase();
    }

    public int getHighestCaseValue()
    {
        if (casesValue.size() == 0)
            return 0;

        int highest = -Integer.MAX_VALUE;

        for (Integer integer : casesValue)
        {
            if (integer > highest)
            {
                highest = integer;
            }
        }

        return highest;
    }

    /**
     * Add output case with the highest value
     */
    public void addOutputCase()
    {
        // Get the highest outputCase
        int highest = getHighestCaseValue();

        addOutputCase(highest);
    }

    /**
     * Add output case entry
     * @param caseValue entry value
     */
    public void addOutputCase(int caseValue)
    {
        // One case has only one output entry

        // Check if the case value is not already present
        assert(!casesValue.contains(caseValue));

        int level = getNumberOfParallelsImbrications();

        // Add at the end, linked functions are ordered by imbrications levels
        addOutputImbricatedEntry(level, level, caseValue + "", Void.class);

        casesValue.add(caseValue);
    }

    /**
     * Change case output imbricated index
     *
     * @param outputIndex output index
     * @param caseValue changed case value
     */
    public void setOutputCaseValue(int outputIndex, int caseValue)
    {
        // Check if it's a case output
        assert(outputIndex > defaultOutputIndex && outputIndex < continueOutputIndex);

        casesValue.set(outputIndex - defaultOutputIndex, caseValue);
    }

    /**
     * Remove output case entry
     * @param outputCaseIndex index of output case
     */
    public void removeOutputCase(int outputCaseIndex)
    {
        removeOutputEntry(outputCaseIndex);

        casesValue.remove(outputCaseIndex - defaultOutputIndex);
    }

    public int getDefaultOutputIndex()
    {
        return defaultOutputIndex;
    }

    public int getContinueOutputIndex()
    {
        return continueOutputIndex;
    }

    @Override
    public void setOutputImbricatedIndex(int caseIndex, int caseValue)
    {
        // If the value is already in switch cases values
        if (imbricationOfOutputs.contains(caseValue))
        {
            // Case with this value already exists
            return;
        }

        // Change the case value
        super.setOutputImbricatedIndex(caseIndex, caseValue);

        // Change the output name
        setOutputName(defaultOutputIndex + caseIndex, caseValue + "");
    }

    @Override
    protected void addOutputImbricatedEntry(int outputIndex, int imbricatedIndex, String outputName, Class cl)
    {
        super.addOutputImbricatedEntry(outputIndex, imbricatedIndex, outputName, cl);

        // Update default output index
        if (outputIndex <= defaultOutputIndex)
            defaultOutputIndex++;

        // Update continue output index
        if (outputIndex <= continueOutputIndex)
            continueOutputIndex++;
    }

    @Override
    public void removeOutputEntry(int outputIndex)
    {
        super.removeOutputEntry(outputIndex);

        // Update default output index
        if (outputIndex < defaultOutputIndex)
            defaultOutputIndex--;

        // Update continue output index
        if (outputIndex < continueOutputIndex)
            continueOutputIndex--;
    }

    @Override
    public Func getFunc()
    {
        return new Func()
        {
            // Holds cases values and corresponding index output
            private Map<Integer, Integer> casesMap = new HashMap<>();

            private int defaultOutputIndex;
            private int continueOutputIndex;

            public Func init(ArrayList<Integer> casesValue, int defaultOutputIndex, int continueOutputIndex)
            {
                this.defaultOutputIndex = defaultOutputIndex;
                this.continueOutputIndex = continueOutputIndex;

                for (int outputIndex = 0; outputIndex < casesValue.size(); outputIndex++)
                {
                    // If it's an imbricated output
                    if (casesValue.get(outputIndex) != -1)
                    {
                        // Add case output index
                        this.casesMap.put(casesValue.get(outputIndex), outputIndex + defaultOutputIndex + 1);
                    }
                }

                return this;
            }

            @Override
            public void run(Program program)
            {
                // Get switch objects parameters
                Object[] objects = program.loadCurrentParametersObjects();

                // Get the source value from objects parameters
                Integer sourceValue = (Integer) objects[SOURCE_INPUT_INDEX];

                // Get addresses of imbricated cases outputs functions
                FuncImbricationDataAddresses funcParameters = program.getCurrentFuncImbricationParameters();

                // Get the corresponding output case
                Integer caseOutputIndex = casesMap.get(sourceValue);

                // Save current state of memory
                program.pushMemoryLevel();

                FuncAddress caseAddressStart, caseAddressEnd;

                // If there is one output
                if (caseOutputIndex != null)
                {
                    // Get start and end of case
                    caseAddressStart = funcParameters.getImbricationAddress(caseOutputIndex);
                    caseAddressEnd = funcParameters.getImbricationAddress(caseOutputIndex + 1);
                }
                else
                {
                    // Get start and end of default case
                    caseAddressStart = funcParameters.getImbricationAddress(this.defaultOutputIndex);
                    caseAddressEnd = funcParameters.getImbricationAddress(this.defaultOutputIndex + 1);
                }

                // Run from the start and end of function
                program.runFromAndUntil(caseAddressStart, caseAddressEnd);

                program.popMemoryLevel();

                // Exit the switch case
                FuncAddress continueAddress = funcParameters.getImbricationAddress(continueOutputIndex);

                program.setNextFuncAddress(continueAddress);
            }
        }.init(casesValue, defaultOutputIndex, continueOutputIndex);
    }
}
