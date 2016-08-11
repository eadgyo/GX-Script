package org.eadge.gxscript.data.entity;

import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;
import org.eadge.gxscript.tools.Tools;
import org.eadge.gxscript.tools.check.exception.NotMatchingInputOutputClasses;

import java.util.*;

/**
 * Created by eadgyo on 10/08/16.
 *
 * Default entity model
 */
public abstract class DefaultEntity implements Entity
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
     * Linked on input entities
     */
    protected ArrayList<Entity> inputEntities = new ArrayList<>();

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
    protected ArrayList<Set<Entity>> outputEntities = new ArrayList<>();

    /**
     * Hold the inputs indices of the outputs entities
     */
    protected ArrayList<Map<Entity, Integer>> inputFromOutputEntitiesIndices = new ArrayList<>();

    public DefaultEntity(String name)
    {
        this.name = name;
    }

    public DefaultEntity()
    {
        this.name = "";
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
    public Entity getInputEntity(int index)
    {
        return inputEntities.get(index);
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

    public Entity getInputEntity(String name)
    {
        int inputIndex = getInputIndex(name);
        if (inputIndex != -1)
            return getInputEntity(inputIndex);
        else
            return null;
    }

    @Override
    public boolean inputContains(int inputIndex, Entity entity)
    {
        return getInputEntity(inputIndex) == entity;
    }

    @Override
    public boolean outputContains(int outputIndex, Entity entity)
    {
        return getOutputEntities(outputIndex).contains(entity);
    }

    @Override
    public int getIndexOfOutputFromEntityOnInput(int inputIndex)
    {
        return outputFromInputEntitiesIndices.get(inputIndex);
    }

    @Override
    public Collection<Entity> getAllInputEntities()
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

    public Collection<Entity> getOutputEntities(String name)
    {
        int outputIndex = getOutputIndex(name);
        if (outputIndex != -1)
            return getOutputEntities(outputIndex);
        else
            return null;
    }

    @Override
    public Collection<Entity> getOutputEntities(int index)
    {
        return outputEntities.get(index);
    }

    @Override
    public int getIndexOfInputFromEntityOnOutput(int outputIndex, Entity outputEntity)
    {
        return inputFromOutputEntitiesIndices.get(outputIndex).get(outputEntity);
    }

    @Override
    public Collection<? extends Collection<Entity>> getAllOutputEntities()
    {
        return outputEntities;
    }

    @Override
    public boolean hasAllNeededInput()
    {
        // Check if all needed input are linked
        for (Integer indexNeededInput : indicesNeededInputs)
        {
            // If there is one input entity
            if (getInputEntity(indexNeededInput) == null)
            {
                // It miss needed input
                return false;
            }
        }

        // All needed inputs has one entity
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
    public boolean isVariableInput(int inputIndex)
    {
        return indicesVariableInputs.contains(inputIndex);
    }

    protected void addInputEntry(String inputName, Class cl)
    {
        addInputEntry(getNumberOfInputs(), inputName, cl);
    }

    protected void addInputEntryNotNeeded(String inputName, Class cl)
    {
        addInputEntryNotNeeded(getNumberOfInputs(), inputName, cl);
    }

    protected void addInputEntry(int inputIndex, String inputName, Class cl)
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
     * Add not needed input entry at the given index
     * @param inputIndex input index
     * @param inputName name of input entry
     * @param cl class of the input, void if it's just a link to another entity
     */
    protected void addInputEntryNotNeeded(int inputIndex, String inputName, Class cl)
    {
        // If add between two existing input entries
        if (inputIndex != getNumberOfInputs())
        {
            addOffsetInput(inputIndex, 1);
        }

        if (cl != Void.class)
        {
            indicesVariableInputs.add(inputIndex);
        }

        inputEntities.add(inputIndex, null);
        outputFromInputEntitiesIndices.add(inputIndex, -1);
        inputClasses.add(inputIndex, cl);
        inputsNames.add(inputIndex, inputName);
    }

    /**
     * Remove one input entry
     * @param inputIndex input index
     */
    protected void removeInputEntry(int inputIndex)
    {
        // Remove linked to entry entities
        unlinkAsInput(inputIndex);

        indicesVariableOutputs.remove(inputIndex);
        inputEntities.remove(inputIndex);
        outputFromInputEntitiesIndices.remove(inputIndex);
        inputClasses.remove(inputIndex);
        inputsNames.remove(inputIndex);

        if (inputIndex != getNumberOfInputs())
        {
            addOffsetInput(inputIndex, -1);
        }
    }

    /**
     * Add offset on input indices
     * @param startInputIndex inclusive start input index
     * @param offset offset to add to index
     */
    protected void addOffsetInput(int startInputIndex, int offset)
    {
        for (int inputIndex = startInputIndex; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            int updatedIndex = inputIndex + offset;

            // If the input is needed
            if (isInputNeeded(inputIndex))
            {
                indicesNeededInputs.remove(inputIndex);
                indicesNeededInputs.add(updatedIndex);
            }

            // If the input is already linked
            Entity inputEntity = getInputEntity(inputIndex);
            if (inputEntity != null)
            {
                int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);
                inputEntity.changeIndexOfInputFromEntityOnOutput(outputIndex, this, updatedIndex);
            }

            // If the input is a variable entry
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
            Collection<Entity> outputEntities = getOutputEntities(outputIndex);
            for (Entity outputEntity : outputEntities)
            {
                int inputIndex = getIndexOfInputFromEntityOnOutput(outputIndex, outputEntity);
                outputEntity.changeIndexOfOutputFromEntityOnInput(inputIndex, updatedOutput);
            }

            // If the output is a variable entry
            if (isVariableOutput(outputIndex))
            {
                indicesVariableOutputs.remove(outputIndex);
                indicesVariableOutputs.add(updatedOutput);
            }
        }
    }

    protected void addOutputEntry(String outputName, Class cl)
    {
        addOutputEntry(getNumberOfOutputs(), outputName, cl);
    }

    /**
     * Add not needed output entry at the given index
     * @param outputIndex output index
     * @param outputName name of output entry
     * @param cl class of the output, void if it's just a link to another entity
     */
    protected void addOutputEntry(int outputIndex, String outputName, Class cl)
    {
        // If add between two existing outputs entries
        if (outputIndex != getNumberOfOutputs())
        {
            addOffsetOutput(outputIndex, 1);
        }

        if (cl != Void.class)
        {
            indicesVariableOutputs.add(outputIndex);
        }

        outputEntities.add(outputIndex, new HashSet<Entity>());
        inputFromOutputEntitiesIndices.add(outputIndex, new HashMap<Entity, Integer>());
        outputClasses.add(outputIndex, cl);
        outputsNames.add(outputIndex, outputName);
    }

    /**
     * Remove one output entry
     * @param outputIndex output index
     */
    protected void removeOutputEntry(int outputIndex)
    {
        // Remove linked to entry entities
        Collection<Entity> removedOutputEntities = getOutputEntities(outputIndex);
        for (Entity outputEntity : removedOutputEntities)
        {
            unlinkAsOutput(outputIndex, outputEntity);
        }

        indicesVariableOutputs.remove(outputIndex);
        outputEntities.remove(outputIndex);
        outputClasses.remove(outputIndex);
        outputsNames.remove(outputIndex);

        if (outputIndex != getNumberOfOutputs())
        {
            addOffsetOutput(outputIndex, -1);
        }
    }

    @Override
    public boolean isVariableOutput(int outputIndex)
    {
        return indicesVariableOutputs.contains(outputIndex);
    }

    @Override
    public void linkAsInput(int inputIndex, int entityOutput, Entity entity)
    {
        try
        {
            // If they have not matching classes
            if (!Tools.isEqualOrDerivedFrom(getInputClass(inputIndex), entity.getOutputClass(entityOutput)))
                throw new NotMatchingInputOutputClasses();
        }
        catch (NotMatchingInputOutputClasses notMatchingInputOutputClasses)
        {
            notMatchingInputOutputClasses.printStackTrace();
        }

        addLinkInput(inputIndex, entityOutput, entity);
        entity.addLinkOutput(entityOutput, inputIndex, this);
    }

    @Override
    public void unlinkAsInput(int inputIndex)
    {
        Entity entity = getInputEntity(inputIndex);
        int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);

        entity.removeLinkOutput(outputIndex, this);
        removeLinkInput(inputIndex);
    }

    @Override
    public void addLinkInput(int inputIndex, int outputEntityIndex, Entity entity)
    {
        inputEntities.set(inputIndex, entity);
        outputFromInputEntitiesIndices.set(inputIndex, outputEntityIndex);
    }

    @Override
    public void removeLinkInput(int inputIndex)
    {
        inputEntities.set(inputIndex, null);
        outputFromInputEntitiesIndices.set(inputIndex, -1);
    }

    @Override
    public void addLinkOutput(int outputIndex, int inputEntityIndex, Entity entity)
    {
        outputEntities.get(outputIndex).add(entity);
        inputFromOutputEntitiesIndices.get(outputIndex).put(entity, inputEntityIndex);
    }

    @Override
    public void removeLinkOutput(int outputIndex, Entity entity)
    {
        outputEntities.get(outputIndex).remove(entity);
        inputFromOutputEntitiesIndices.get(outputIndex).remove(entity);
    }

    @Override
    public void changeIndexOfOutputFromEntityOnInput(int inputIndex, int newOutputIndex)
    {
        outputFromInputEntitiesIndices.set(inputIndex, newOutputIndex);
    }

    @Override
    public void changeIndexOfInputFromEntityOnOutput(int outputIndex, Entity outputEntity, int newInputIndex)
    {
        inputFromOutputEntitiesIndices.get(outputIndex).put(outputEntity, newInputIndex);
    }

    /**
     * Create collection of addresses containing parameters addresses of function
     * @param addressesMap map to get input memory stack
     * @return created parameters addresses of function
     */
    protected FuncDataAddresses createFuncDataAddresses(Map<Entity, OutputAddresses> addressesMap)
    {
        FuncDataAddresses funcDataAddresses = new FuncDataAddresses(getNumberOfInputs());
        initFuncDataAddresses(addressesMap, funcDataAddresses);
        return funcDataAddresses;
    }

    /**
     * Fill parameters addresses of funcion
     * @param addressesMap map to get input memory stack
     * @param funcDataAddresses filled parameters addresses of function
     */
    protected void initFuncDataAddresses(Map<Entity, OutputAddresses> addressesMap, FuncDataAddresses funcDataAddresses)
    {
        // For each variable input
        for (int inputIndex = 0, variableIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            // If the input is taking variable
            if (isVariableInput(inputIndex))
            {
                // Get variable address
                Entity inputEntity = getInputEntity(inputIndex);

                // Get corresponding outputs addresses
                OutputAddresses outputAddresses = addressesMap.get(inputEntity);

                // Get the output index of this variable
                int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);

                // Set address variable address
                funcDataAddresses.setInputAddress(variableIndex, outputAddresses.getOutputAddress(outputIndex));

                variableIndex++;
            }
        }
    }


    /**
     * Create a link between this entity considered as output and one other input entity
     * @param outputIndex output index
     * @param entityInput other entity input index
     * @param entity input entity
     */
    public void linkAsOutput(int outputIndex, int entityInput, Entity entity)
    {
        entity.linkAsInput(entityInput, outputIndex, this);
    }

    /**
     * Remove link on output at the given output index
     * @param outputIndex output index
     * @param entity removed link on output entity
     */
    public void unlinkAsOutput(int outputIndex, Entity entity)
    {
        int inputIndex = getIndexOfInputFromEntityOnOutput(outputIndex, entity);
        entity.unlinkAsInput(inputIndex);
    }

    /**
     * Default add funcs and funcs params
     *
     * @param calledFunctions          list of called function
     * @param calledFunctionAddresses list of used called function data
     * @param addressesMap                      map to link entity to corresponding entity output addresses
     */
    public void addFuncsAndSaveOutputs(ArrayList<Func> calledFunctions,
                                       ArrayList<FuncDataAddresses> calledFunctionAddresses,
                                       Map<Entity, OutputAddresses> addressesMap)
    {
        // Get func and add it to called functions
        Func func = getFunc();
        calledFunctions.add(func);

        // Link input to corresponding outputs addresses and add it to called function addresses
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
     * @param addressesMap map entity to outputAddresses
     * @return created and linked func data addresses
     */
    public FuncDataAddresses createAndLinkFuncDataAddresses(Map<Entity, OutputAddresses> addressesMap)
    {
        // Create function data addresses (inputs of the entity)
        FuncDataAddresses funcDataAddresses = new FuncDataAddresses(getNumberOfVariableInput());

        // Link data addresses
        for (int inputIndex = 0, variableIndex = 0; inputIndex < getNumberOfInputs(); inputIndex++)
        {
            if (isVariableInput(inputIndex))
            {
                Entity inputEntity = getInputEntity(inputIndex);

                // If the entity is linked at this input
                if (inputEntity != null)
                {
                    // Get the corresponding outputs addresses
                    OutputAddresses outputAddresses = addressesMap.get(inputEntity);

                    assert (outputAddresses != null);

                    int outputIndex = getIndexOfOutputFromEntityOnInput(inputIndex);

                    // Get the address of the corresponding output/input link
                    DataAddress outputAddress = outputAddresses.getOutputAddress(outputIndex);

                    // Store the output address, use variable index instead of input index
                    funcDataAddresses.setInputAddress(variableIndex, outputAddress);
                }
                else
                {
                    // Use variable index instead of input index, funcDataAddresses doesn't contain non
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
    public Collection<Entity> getAllOutputEntitiesCollection()
    {
        Collection<Entity>             allOutputEntities          = new ArrayList<>();
        Collection<? extends Collection<Entity>> allOutputEntitiesSeparated = getAllOutputEntities();

        for (Collection<Entity> entities : allOutputEntitiesSeparated)
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
        Collection<Entity> outputEntities = getOutputEntities(index);

        // For all connected blocks
        for (Entity outputEntity : outputEntities)
        {
            // Get his linked index
            int indexOfInputEntity = getIndexOfInputFromEntityOnOutput(index, outputEntity);

            // If they are partially linked
            if (indexOfInputEntity == -1)
            {
                // There is a problem
                return false;
            }

            // If they have no matching intput/output types
            if (!Tools.isEqualOrDerivedFrom(outputEntity.getInputClass(indexOfInputEntity), getOutputClass(index)))
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
            // If the input is not valid
            if (!isValidInput(index))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the input at the index is valid
     *
     * @param inputIndex input index
     *
     * @return true if the input is valid, false otherwise
     */
    public boolean isValidInput(int inputIndex)
    {
        Entity inputEntity = getInputEntity(inputIndex);

        // If input is not linked
        if (inputEntity == null)
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
        if (!Tools.isEqualOrDerivedFrom(getInputClass(inputIndex), inputEntity.getInputClass(indexOfOutputEntity)))
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
            // If the input is not valid
            if (!isValidOutput(index))
            {
                return false;
            }
        }
        return true;
    }
}
