package org.eadge.gxscript.data.entity;

import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;
import org.eadge.gxscript.tools.Tools;
import org.eadge.gxscript.data.exception.NotMatchingInputOutputClasses;

import java.util.*;

/**
 * Created by eadgyo on 10/08/16.
 *
 * Default GXEntity model
 */
public abstract class DefaultGXEntity implements GXEntity
{
    protected String name;

    /**
     * Names of inputs
     */
    protected ArrayList<String> inputsNames = new ArrayList<>();

    /**
     * Indices of all needed inputs
     */
    protected Set<Integer> indicesNeededInputs = new HashSet<>();

    /**
     * Indices of all inputs taking variables
     */
    protected Set<Integer> indicesVariableInputs = new HashSet<>();

    /**
     * Classes of all inputs
     */
    protected ArrayList<Class> inputClasses = new ArrayList<>();

    /**
     * Linked on function entities
     */
    protected ArrayList<GXEntity> inputEntities = new ArrayList<>();

    /**
     * Names of outputs
     */
    protected ArrayList<String> outputsNames = new ArrayList<>();

    /**
     * Hold the outputs indices of the inputs entities
     */
    protected ArrayList<Integer> outputFromInputEntitiesIndices = new ArrayList<>();

    /**
     * Indices of all outputs returning variables
     */
    protected Set<Integer> indicesVariableOutputs = new HashSet<>();

    /**
     * Classes of all outputs
     */
    protected ArrayList<Class> outputClasses = new ArrayList<>();

    /**
     * Linked on outputs entities
     */
    protected ArrayList<Set<GXEntity>> outputEntities = new ArrayList<>();

    /**
     * Hold the inputs indices of the outputs entities
     */
    protected ArrayList<Map<GXEntity, Integer>> inputFromOutputEntitiesIndices = new ArrayList<>();

    public DefaultGXEntity()
    {
        this("");
    }

    public DefaultGXEntity(String name)
    {
        this.name = name;
    }

    /*public DefaultGXEntity()
    {
        this.name = "";
    }*/

    @Override
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInputName(int index) { return inputsNames.get(index); }

    @Override
    public Class getInputClass(int index)
    {
        return inputClasses.get(index);
    }

    @Override
    public Collection<Class> getAllInputClasses()
    {
        return inputClasses;
    }

    @Override
    public int getNumberOfInputs()
    {
        return inputClasses.size();
    }

    @Override
    public int getNumberOfUsedInputs()
    {
        int usedInput = 0;

        for (int inputIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            // If the function is used
            if (getInputEntity(inputIndex) != null)
            {
                usedInput++;
            }
        }

        return usedInput;
    }

    @Override
    public boolean hasInputsUsed()
    {
        for (int inputIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            // If the function is used
            if (getInputEntity(inputIndex) != null)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public GXEntity getInputEntity(int index)
    {
        return inputEntities.get(index);
    }

    @Override
    public boolean isInputUsed(int index)
    {
        return getInputEntity(index) != null;
    }

    public int getInputIndex(String name)
    {
        for (int inputIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            if (getInputName(inputIndex).equals(name))
            {
                return inputIndex;
            }
        }
        return -1;
    }

    public GXEntity getInputEntity(String name)
    {
        int inputIndex = getInputIndex(name);
        if (inputIndex != -1)
            return getInputEntity(inputIndex);
        else
            return null;
    }

    @Override
    public boolean inputContains(int inputIndex, GXEntity GXEntity)
    {
        return getInputEntity(inputIndex) == GXEntity;
    }

    @Override
    public boolean outputContains(int outputIndex, GXEntity GXEntity)
    {
        return getOutputEntities(outputIndex).contains(GXEntity);
    }

    @Override
    public int getIndexOfOutputFromEntityOnInput(int inputIndex)
    {
        return outputFromInputEntitiesIndices.get(inputIndex);
    }

    @Override
    public Class getOutputClassFromInputEntity(int inputIndex)
    {
        int outputIndexFromInputEntity = getIndexOfOutputFromEntityOnInput(inputIndex);

        if (outputIndexFromInputEntity != -1)
        {
            return getInputEntity(inputIndex).getOutputClass(outputIndexFromInputEntity);
        }
        return null;
    }

    @Override
    public Collection<GXEntity> getAllInputEntities()
    {
        return inputEntities;
    }

    @Override
    public Class getOutputClass(int index)
    {
        return outputClasses.get(index);
    }

    @Override
    public Collection<Class> getAllOutputClasses()
    {
        return outputClasses;
    }

    @Override
    public int getNumberOfOutputs()
    {
        return outputClasses.size();
    }

    public String getOutputName(int index)
    {
        return outputsNames.get(index);
    }

    public int getOutputIndex(String name)
    {
        for (int outputIndex = 0; outputIndex < getNumberOfOutputs(); outputIndex++)
        {
            if (getOutputName(outputIndex).equals(name))
            {
                return outputIndex;
            }
        }
        return -1;

    }

    public Collection<GXEntity> getOutputEntities(String name)
    {
        int outputIndex = getOutputIndex(name);
        if (outputIndex != -1)
            return getOutputEntities(outputIndex);
        else
            return null;
    }


    @Override
    public boolean isOutputUsed(int index)
    {
        return getOutputEntities(index).size() != 0;
    }

    @Override
    public Collection<GXEntity> getOutputEntities(int index)
    {
        return outputEntities.get(index);
    }

    @Override
    public int getIndexOfInputFromEntityOnOutput(int outputIndex, GXEntity outputGXEntity)
    {
        return inputFromOutputEntitiesIndices.get(outputIndex).get(outputGXEntity);
    }

    @Override
    public Collection<? extends Collection<GXEntity>> getAllOutputEntities()
    {
        return outputEntities;
    }

    @Override
    public boolean hasAllNeededInput()
    {
        // Check if all needed function are linked
        for (Integer indexNeededInput : indicesNeededInputs)
        {
            // If there is one function GXEntity
            if (getInputEntity(indexNeededInput) == null)
            {
                // It miss needed function
                return false;
            }
        }

        // All needed inputs has one GXEntity
        return true;
    }

    @Override
    public int getNumberOfVariableOutput()
    {
        return indicesVariableOutputs.size();
    }


    @Override
    public int getNumberOfVariableInput()
    {
        return indicesVariableInputs.size();
    }

    @Override
    public int getNumberOfUsedVariableInput()
    {
        int numberOfUsedVariableInput = 0;

        for (Integer indexVariableInput : indicesVariableInputs)
        {
            if (isInputUsed(indexVariableInput))
            {
                numberOfUsedVariableInput++;
            }
        }

        return numberOfUsedVariableInput;
    }

    @Override
    public boolean isVariableInput(int inputIndex)
    {
        return indicesVariableInputs.contains(inputIndex);
    }

    public void addInputEntry(String inputName, Class cl)
    {
        addInputEntry(getNumberOfInputs(), inputName, cl);
    }

    public void addInputEntryNotNeeded(String inputName, Class cl)
    {
        addInputEntryNotNeeded(getNumberOfInputs(), inputName, cl);
    }

    public void addInputEntry(int inputIndex, String inputName, Class cl)
    {
        addInputEntryNotNeeded(inputIndex, inputName, cl);
        indicesNeededInputs.add(inputIndex);
    }

    @Override
    public boolean isInputNeeded(int inputIndex)
    {
        return indicesNeededInputs.contains(inputIndex);
    }

    @Override
    public Collection<Integer> getAllInputsNeeded()
    {
        return indicesNeededInputs;
    }

    /**
     * Add not needed function entry at the given index
     * @param inputIndex function index
     * @param inputName name of function entry
     * @param cl class of the function, void if it's just a link to another GXEntity
     */
    public void addInputEntryNotNeeded(int inputIndex, String inputName, Class cl)
    {
        // If add between two existing function entries
        if (inputIndex != getNumberOfInputs())
        {
            addOffsetInput(inputIndex, 1);
        }

        // If the function entry is not a function linker
        if (cl != Void.class)
        {
            // Input entry is a variable entry
            indicesVariableInputs.add(inputIndex);
        }

        // Add data associated with function entry
        inputEntities.add(inputIndex, null);
        outputFromInputEntitiesIndices.add(inputIndex, -1);
        inputClasses.add(inputIndex, cl);
        inputsNames.add(inputIndex, inputName);
    }


    /**
     * Change function entry name
     *
     * @param inputIndex function index
     * @param inputName new function name
     */
    public void setInputName(int inputIndex, String inputName)
    {
        inputsNames.set(inputIndex, inputName);
    }

    /**
     * Change set function class and remove function if the new class is not compatible with the old one
     * @param inputIndex function entry index
     * @param cl new class
     */
    public void setInputClass(int inputIndex, Class cl)
    {
        // If new link class is not compatible with the last one
        if (!Tools.isEqualOrDerivedFrom(cl, getInputClass(inputIndex)))
        {
            // Remove function linked entities
            unlinkAsInput(inputIndex);
        }

        // Change function class
        inputClasses.set(inputIndex, cl);
    }

    /**
     * Remove linked function GXEntity
     *
     * @param inputIndex function index
     */
    public void clearLinkedInput(int inputIndex)
    {
        // Remove linked to function entry entities
        unlinkAsInput(inputIndex);
    }

    /**
     * Remove one function entry
     * @param inputIndex function index
     */
    public void removeInputEntry(int inputIndex)
    {
        // Remove linked to entry entities
        clearLinkedInput(inputIndex);

        // Remove associated data with function entry
        indicesVariableOutputs.remove(inputIndex);
        inputEntities.remove(inputIndex);
        outputFromInputEntitiesIndices.remove(inputIndex);
        inputClasses.remove(inputIndex);
        inputsNames.remove(inputIndex);

        // If the function is not the last
        if (inputIndex != getNumberOfInputs())
        {
            // Update inputs indices of upper inputs
            addOffsetInput(inputIndex, -1);
        }
    }

    /**
     * Add offset on function indices
     * @param startInputIndex inclusive start function index
     * @param offset offset to add to index
     */
    protected void addOffsetInput(int startInputIndex, int offset)
    {
        for (int inputIndex = startInputIndex; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            int updatedIndex = inputIndex + offset;

            // If the function is needed
            if (isInputNeeded(inputIndex))
            {
                indicesNeededInputs.remove(inputIndex);
                indicesNeededInputs.add(updatedIndex);
            }

            // If the function is already linked
            GXEntity inputGXEntity = getInputEntity(inputIndex);
            if (inputGXEntity != null)
            {
                int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);
                inputGXEntity.changeIndexOfInputFromEntityOnOutput(outputIndex, this, updatedIndex);
            }

            // If the function is a variable entry
            if (isVariableInput(inputIndex))
            {
                indicesVariableInputs.remove(inputIndex);
                indicesVariableInputs.add(updatedIndex);
            }
        }
    }

    /**
     * Add offset on output indices
     * @param startOutputIndex inclusive start output index
     * @param offset offset to add to index
     */
    protected void addOffsetOutput(int startOutputIndex, int offset)
    {
        for (int outputIndex = startOutputIndex; outputIndex < getNumberOfOutputs(); outputIndex++)
        {
            int updatedOutput = outputIndex + offset;

            // Update all linked outputs entities
            Collection<GXEntity> outputEntities = getOutputEntities(outputIndex);
            for (GXEntity outputGXEntity : outputEntities)
            {
                int inputIndex = getIndexOfInputFromEntityOnOutput(outputIndex, outputGXEntity);
                outputGXEntity.changeIndexOfOutputFromEntityOnInput(inputIndex, updatedOutput);
            }

            // If the output is a variable entry
            if (isVariableOutput(outputIndex))
            {
                indicesVariableOutputs.remove(outputIndex);
                indicesVariableOutputs.add(updatedOutput);
            }
        }
    }

    public void addOutputEntry(String outputName, Class cl)
    {
        addOutputEntry(getNumberOfOutputs(), outputName, cl);
    }

    /**
     * Add not needed output entry at the given index
     * @param outputIndex output index
     * @param outputName name of output entry
     * @param cl class of the output, void if it's just a link to another GXEntity
     */
    public void addOutputEntry(int outputIndex, String outputName, Class cl)
    {
        // If add between two existing outputs entries
        if (outputIndex != getNumberOfOutputs())
        {
            addOffsetOutput(outputIndex, 1);
        }

        // If the output entry is not a function linker
        if (cl != Void.class)
        {
            // Output entry is a variable entry
            indicesVariableOutputs.add(outputIndex);
        }

        outputEntities.add(outputIndex, new HashSet<GXEntity>());
        inputFromOutputEntitiesIndices.add(outputIndex, new HashMap<GXEntity, Integer>());
        outputClasses.add(outputIndex, cl);
        outputsNames.add(outputIndex, outputName);
    }

    /**
     * Change output entry name
     *
     * @param outputIndex output index
     * @param outputName new output name
     */
    public void setOutputName(int outputIndex, String outputName)
    {
        outputsNames.set(outputIndex, outputName);
    }

    /**
     * Change set output class and remove outputs if the new class is not compatible with the old one
     * @param outputIndex output entry index
     * @param cl new class
     */
    public void setOutputClass(int outputIndex, Class cl)
    {
        // If new link class is not compatible with the last one
        if (!Tools.isEqualOrDerivedFrom(cl, getOutputClass(outputIndex)))
        {
            // Remove output linked entities
            unlinkAsOutput(outputIndex);
        }

        // Change function class
        inputClasses.set(outputIndex, cl);
    }

    /**
     * Remove all linked output entities
     *
     * @param outputIndex output index
     */
    public void clearLinkedOutputs(int outputIndex)
    {
        // Remove linked to entry entities
        Collection<GXEntity> removedOutputEntities = getOutputEntities(outputIndex);
        for (GXEntity outputGXEntity : removedOutputEntities)
        {
            unlinkAsOutput(outputIndex, outputGXEntity);
        }
    }

    /**
     * Remove one output entry
     * @param outputIndex output index
     */
    public void removeOutputEntry(int outputIndex)
    {
        // Remove all linked entities
        clearLinkedOutputs(outputIndex);

        // Remove all output data
        indicesVariableOutputs.remove(outputIndex);
        outputEntities.remove(outputIndex);
        outputClasses.remove(outputIndex);
        outputsNames.remove(outputIndex);

        // If the output is the not the last
        if (outputIndex != getNumberOfOutputs())
        {
            // Update all other upper output entries indices
            addOffsetOutput(outputIndex, -1);
        }
    }

    @Override
    public boolean isVariableOutput(int outputIndex)
    {
        return indicesVariableOutputs.contains(outputIndex);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, GXEntity GXEntity)
    {
        try
        {
            // Get classes
            Class inputClass = getInputClass(inputIndex);
            Class outputClass = GXEntity.getOutputClass(entityOutput);

            // If they have not matching classes
            if (!Tools.isEqualOrDerivedFrom(outputClass, inputClass))
                throw new NotMatchingInputOutputClasses();

            addLinkInput(inputIndex, entityOutput, GXEntity);
            GXEntity.addLinkOutput(entityOutput, inputIndex, this);
        }
        catch (NotMatchingInputOutputClasses notMatchingInputOutputClasses)
        {
            notMatchingInputOutputClasses.printStackTrace();
        }
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        GXEntity GXEntity    = getInputEntity(inputIndex);
        int      outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);

        GXEntity.removeLinkOutput(outputIndex, this);
        removeLinkInput(inputIndex);
    }

    @Override
    public void addLinkInput(int inputIndex, int outputEntityIndex, GXEntity GXEntity)
    {
        inputEntities.set(inputIndex, GXEntity);
        outputFromInputEntitiesIndices.set(inputIndex, outputEntityIndex);
    }

    @Override
    public void removeLinkInput(int inputIndex)
    {
        inputEntities.set(inputIndex, null);
        outputFromInputEntitiesIndices.set(inputIndex, -1);
    }

    @Override
    public void addLinkOutput(int outputIndex, int inputEntityIndex, GXEntity GXEntity)
    {
        outputEntities.get(outputIndex).add(GXEntity);
        inputFromOutputEntitiesIndices.get(outputIndex).put(GXEntity, inputEntityIndex);
    }

    @Override
    public void removeLinkOutput(int outputIndex, GXEntity GXEntity)
    {
        outputEntities.get(outputIndex).remove(GXEntity);
        inputFromOutputEntitiesIndices.get(outputIndex).remove(GXEntity);
    }

    @Override
    public void changeIndexOfOutputFromEntityOnInput(int inputIndex, int newOutputIndex)
    {
        outputFromInputEntitiesIndices.set(inputIndex, newOutputIndex);
    }

    @Override
    public void changeIndexOfInputFromEntityOnOutput(int outputIndex, GXEntity outputGXEntity, int newInputIndex)
    {
        inputFromOutputEntitiesIndices.get(outputIndex).put(outputGXEntity, newInputIndex);
    }

    /**
     * Create collection of addresses containing parameters addresses of function
     * @param addressesMap map to get function memory stack
     * @return created parameters addresses of function
     */
    protected FuncDataAddresses createFuncDataAddresses(Map<GXEntity, OutputAddresses> addressesMap)
    {
        FuncDataAddresses funcDataAddresses = new FuncDataAddresses(getNumberOfUsedInputs());
        initFuncDataAddresses(addressesMap, funcDataAddresses);
        return funcDataAddresses;
    }

    /**
     * Fill parameters Data addresses of function
     * @param addressesMap map to get function memory stack
     * @param funcDataAddresses filled Data parameters addresses for function call
     */
    protected void initFuncDataAddresses(Map<GXEntity, OutputAddresses> addressesMap, FuncDataAddresses funcDataAddresses)
    {
        // For each variable function
        for (int inputIndex = 0, variableIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            // If the function is taking variable and is used
            if (isVariableInput(inputIndex) && isInputUsed(inputIndex))
            {
                // Get variable address
                GXEntity inputGXEntity = getInputEntity(inputIndex);

                // Get corresponding outputs addresses
                OutputAddresses outputAddresses = addressesMap.get(inputGXEntity);

                // Get the output index of this variable
                int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);

                // Set address variable address
                funcDataAddresses.setInputAddress(variableIndex, outputAddresses.getOutputAddress(outputIndex));

                variableIndex++;
            }
        }
    }

    /**
     * Create a link between this GXEntity considered as output and one other function GXEntity
     * @param outputIndex output index
     * @param entityInput other GXEntity function index
     * @param GXEntity function GXEntity
     */
    public void linkAsOutput(int outputIndex, int entityInput, GXEntity GXEntity)
    {
        GXEntity.linkAsInput(entityInput, outputIndex, this);
    }

    /**
     * Remove link on output at the given output index
     * @param outputIndex output index
     * @param GXEntity removed link on output GXEntity
     */
    public void unlinkAsOutput(int outputIndex, GXEntity GXEntity)
    {
        int inputIndex = getIndexOfInputFromEntityOnOutput(outputIndex, GXEntity);
        GXEntity.unlinkAsInput(inputIndex);
    }

    /**
     * Remove all linked entities on output at the given output index
     *
     * @param outputIndex output index
     */
    public void unlinkAsOutput(int outputIndex)
    {
        for (GXEntity outputGXEntity : getOutputEntities(outputIndex))
        {
            unlinkAsOutput(outputIndex, outputGXEntity);
        }
    }

    /**
     * Default add funcs and funcs params
     *
     * @param calledFunctions          list of called function
     * @param calledFunctionAddresses list of used called function data
     * @param addressesMap                      map to link GXEntity to corresponding GXEntity output addresses
     */
    public void pushEntityCode(ArrayList<Func> calledFunctions,
                               ArrayList<FuncDataAddresses> calledFunctionAddresses,
                               Map<GXEntity, OutputAddresses> addressesMap)
    {
        // Get func and add it to called functions
        Func func = getFunc();
        calledFunctions.add(func);

        // Link function to corresponding outputs addresses and add it to called function addresses
        FuncDataAddresses funcDataAddresses = createAndLinkFuncDataAddresses(addressesMap);
        calledFunctionAddresses.add(funcDataAddresses);
    }

    /**
     * Create outputs and alloc outputs in stack
     * @param currentDataAddress current address on stack of data addresses
     * @return created output addresses
     */
    public OutputAddresses createAndAllocOutputs(DataAddress currentDataAddress)
    {
        OutputAddresses outputAddresses = new OutputAddresses();

        for (int outputIndex = 0; outputIndex < getNumberOfOutputs(); outputIndex++)
        {
            if (isVariableOutput(outputIndex))
            {
                // Take this address
                outputAddresses.addOutputAddress(outputIndex, currentDataAddress.clone());

                // Alloc a new address
                currentDataAddress.alloc();
            }
        }

        return outputAddresses;
    }

    /**
     * Create funcDataAddresses and link inputs to the corresponding outputs addresses
     * @param addressesMap map GXEntity to outputAddresses
     * @return created and linked func data addresses
     */
    public FuncDataAddresses createAndLinkFuncDataAddresses(Map<GXEntity, OutputAddresses> addressesMap)
    {
        // Create function data addresses (inputs of the GXEntity)
        FuncDataAddresses funcDataAddresses = new FuncDataAddresses(getNumberOfUsedVariableInput());

        // Link data addresses
        for (int inputIndex = 0, variableIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            if (isVariableInput(inputIndex) && isInputUsed(inputIndex))
            {
                GXEntity inputGXEntity = getInputEntity(inputIndex);

                // If the GXEntity is linked at this function
                if (inputGXEntity != null)
                {
                    // Get the corresponding outputs addresses
                    OutputAddresses outputAddresses = addressesMap.get(inputGXEntity);

                    assert (outputAddresses != null);

                    int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);

                    // Get the address of the corresponding output/function link
                    DataAddress outputAddress = outputAddresses.getOutputAddress(outputIndex);

                    // Store the output address, use variable index instead of function index
                    funcDataAddresses.setInputAddress(variableIndex, outputAddress);
                }
                else
                {
                    // Use variable index instead of function index, funcDataAddresses doesn't contain non
                    // variables inputs
                    funcDataAddresses.setInputAddress(variableIndex, null);
                }

                variableIndex++;
            }
        }
        return funcDataAddresses;
    }
    /**
     * Get all the output entities
     *
     * @return output entities
     */
    public Collection<GXEntity> getAllOutputEntitiesCollection()
    {
        Collection<GXEntity>                       allOutputEntities          = new ArrayList<>();
        Collection<? extends Collection<GXEntity>> allOutputEntitiesSeparated = getAllOutputEntities();

        for (Collection<GXEntity> entities : allOutputEntitiesSeparated)
        {
            allOutputEntities.addAll(entities);
        }
        return allOutputEntities;
    }

    /**
     * Check if the output at the index is valid
     *
     * @param index output index
     *
     * @return true if the output is valid, false otherwise
     */
    public boolean isValidOutput(int index)
    {
        Collection<GXEntity> outputEntities = getOutputEntities(index);

        // For all connected blocks
        for (GXEntity outputGXEntity : outputEntities)
        {
            // Get his linked index
            int indexOfInputEntity = getIndexOfInputFromEntityOnOutput(index, outputGXEntity);

            // If they are partially linked
            if (indexOfInputEntity == -1)
            {
                // There is a problem
                return false;
            }

            // If they have no matching intput/output types
            if (!Tools.isEqualOrDerivedFrom(getOutputClass(index), outputGXEntity.getInputClass(indexOfInputEntity)))
            {
                // Types are not matching
                return false;
            }
        }
        return true;
    }

    /**
     * Check if all the inputs of are valid
     *
     * @return true if inputs are valid, false otherwise
     */
    public boolean hasValidInput()
    {
        // Check for all inputs
        for (int index = 0; index < getNumberOfInputs(); index++)
        {
            // If the function is not valid
            if (!isValidInput(index))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the function at the index is valid
     *
     * @param inputIndex function index
     *
     * @return true if the function is valid, false otherwise
     */
    public boolean isValidInput(int inputIndex)
    {
        GXEntity inputGXEntity = getInputEntity(inputIndex);

        // If function is not linked
        if (inputGXEntity == null)
            return true;

        // Get his linked index
        int indexOfOutputEntity = getIndexOfOutputFromEntityOnInput(inputIndex);

        // If they are partially linked
        if (indexOfOutputEntity == -1)
        {
            // There is a problem
            return false;
        }

        // If they have no matching intput/output types
        if (!Tools.isEqualOrDerivedFrom(inputGXEntity.getOutputClass(indexOfOutputEntity), getInputClass(inputIndex)))
        {
            // Types are not matching
            return false;
        }

        return true;
    }



    /**
     * Check if all the outputs of are valid
     *
     * @return true if outputs are valid, false otherwise
     */
    public boolean hasValidOutput()
    {
        // Check for all outputs
        for (int index = 0; index < getNumberOfOutputs(); index++)
        {
            // If the function is not valid
            if (!isValidOutput(index))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "GXEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
