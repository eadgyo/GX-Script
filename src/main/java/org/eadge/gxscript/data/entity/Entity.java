package org.eadge.gxscript.data.entity;

import org.eadge.gxscript.data.script.Func;
import org.eadge.gxscript.data.script.address.Address;
import org.eadge.gxscript.data.script.address.DataAddress;
import org.eadge.gxscript.data.script.address.FuncDataAddresses;
import org.eadge.gxscript.data.script.address.OutputAddresses;

import java.util.*;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Parent Entity model
 *
 * Entity can have one entity per input and multiples entities per output
 */
public abstract class Entity
{
    //----------------------------
    //---------- Input -----------
    //----------------------------

    /**
     * Get the class of the input object at the index
     *
     * @param index input index
     *
     * @return class of the input object at the index
     */
    public abstract Class getInputClass(int index);

    /**
     * Get all the classes of the input objects
     *
     * @return classes of the input objects
     */
    public abstract Collection<Class> getAllInputClasses();

    /**
     * Get the number of input objects
     *
     * @return number of input objects
     */
    public abstract int getNumberOfInputs();

    /**
     * Check if one entity is contained at the input
     * @param inputIndex input index
     * @param entity tested entity
     * @return true if entity is contained at input index, false otherwise
     */
    public abstract boolean inputContains(int inputIndex, Entity entity);

    /**
     * Get the input entity at the index or null if there are no entities
     *
     * @param index input index
     *
     * @return input entity at the index or null if there are no entities
     */
    public abstract Entity getInputEntity(int index);

    /**
     * Get the index of the output linked to one entity or -1 if it's not a output entity
     *
     * @param inputIndex used input index
     *
     * @return index of the linked to output entity, or -1 if entity is not link on output
     */
    public abstract int getIndexOfOutputFromEntityOnInput(int inputIndex);

    /**
     * Get all the input entities
     *
     * @return all input entities
     */
    public abstract Collection<Entity> getAllInputEntities();

    /**
     * Check if entity have necessary input
     *
     * @return true if entity have enough input, false otherwise
     */
    public abstract boolean hasAllNeededInput();

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
        if (getInputClass(inputIndex) != inputEntity.getInputClass(indexOfOutputEntity))
        {
            // Types are not matching
            return false;
        }

        return true;
    }

    /**
     * Add link on input
     * DO NOT USE THIS ALONE
     * @param inputIndex output index
     * @param entity added entity
     * @param entityOutputIndex corresponding output index
     */
    public abstract void addLinkInput(int inputIndex, int entityOutputIndex, Entity entity);

    /**
     * Remove one entity link on input at the given index
     * DO NOT USE THIS ALONE
     * @param inputIndex input entry to remove entity
     */
    public abstract void removeLinkInput(int inputIndex);

    /**
     * Modify the information on the linked index of one inputEntity
     * @param inputIndex input index
     * @param newOutputIndex updated output index
     */
    public abstract void changeIndexOfOutputFromEntityOnInput(int inputIndex, int newOutputIndex);

    /**
     * Create a link between this entity considered as input and one other output entity
     * @param inputIndex input index
     * @param entityOutput other entity output index
     * @param entity output entity
     */
    public abstract void linkAsInput(int inputIndex, int entityOutput, Entity entity);


    /**
     * Remove link on input at the given input index
     * @param inputIndex input index
     */
    public abstract void unlinkAsInput(int inputIndex);

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
     * Get number of output returning variables
     *
     * @return number of output returning variables
     */
    public abstract int getNumberOfVariableOutput();

    /**
     * Check if an input takes variable
     * @param inputIndex input index
     * @return true if input takes variable, false otherwise
     */
    public abstract boolean isVariableInput(int inputIndex);

    /**
     * Check if an input at the given index is necessary
     * @param inputIndex input index
     * @return true if the input at index is needed, false otherwise
     */
    public abstract boolean isInputNeeded(int inputIndex);

    /**
     * Get all necessary inputs index
     * @return all necessary inputs index
     */
    public abstract Collection<Integer> getAllInputsNeeded();

    //----------------------------
    //----------- Output ---------
    //----------------------------

    /**
     * Get the class of the output object at the index
     *
     * @param index output index
     *
     * @return class of the output object at the index
     */
    public abstract Class getOutputClass(int index);

    /**
     * Get all the classes of the output objects
     *
     * @return classes of the output objects in collection
     */
    public abstract Collection<Class> getAllOutputClasses();

    /**
     * Get the number of output objects
     *
     * @return number of output objects
     */
    public abstract int getNumberOfOutputs();

    /**
     * Get the output entities at the index
     *
     * @param index output index
     *
     * @return output entities at the index
     */
    public abstract Collection<Entity> getOutputEntities(int index);

    /**
     * Check if one entity is contained at the output
     * @param outputIndex output index
     * @param entity tested entity
     * @return true if entity is contained at output index, false otherwise
     */
    public abstract boolean outputContains(int outputIndex, Entity entity);

    /**
     * Get the index of the input linked to one entity or -1 if it's not a input entity
     *
     * @param index  entity output index
     * @param outputEntity linked to input entity
     *
     * @return index of the linked to input entity, or -1 if entity is not link on input
     */
    public abstract int getIndexOfInputFromEntityOnOutput(int index, Entity outputEntity);

    /**
     * Get all the output entities
     *
     * @return output entities keeping separated by output lane
     */
    public abstract Collection<? extends Collection<Entity>> getAllOutputEntities();

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
            if (outputEntity.getInputClass(indexOfInputEntity) != getOutputClass(index))
            {
                // Types are not matching
                return false;
            }
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

    /**
     * Get number of input taking variables
     *
     * @return number of input taking variables
     */
    public abstract int getNumberOfVariableInput();

    /**
     * Check if an output gives variable
     * @param outputIndex output index
     * @return true if output gives variable, false otherwise
     */
    public abstract boolean isVariableOutput(int outputIndex);

    /**
     * Remove one entity link on output at the given index
     * DO NOT USE THIS ALONE
     * @param outputIndex output entry to remove entity
     * @param entity removed entity
     */
    public abstract void removeLinkOutput(int outputIndex, Entity entity);

    /**
     * Modify the information on the linked index of one outputEntity
     * @param outputIndex output index
     * @param outputEntity output entity
     * @param newInputIndex updated input index
     */
    public abstract void changeIndexOfInputFromEntityOnOutput(int outputIndex, Entity outputEntity, int newInputIndex);

    /**
     * Add link on output
     * DO NOT USE THIS ALONE
     * @param outputIndex output index
     * @param inputEntityIndex corresponding input index
     * @param entity added entity
     */
    public abstract void addLinkOutput(int outputIndex, int inputEntityIndex, Entity entity);

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
     * Get the func of the entity
     *
     * @return used func
     */
    public abstract Func getFunc();

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
    protected FuncDataAddresses createAndLinkFuncDataAddresses(Map<Entity, OutputAddresses> addressesMap)
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
                    Address outputAddress = outputAddresses.getOutputAddress(outputIndex);

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
}