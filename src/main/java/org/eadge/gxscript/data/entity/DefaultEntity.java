package org.eadge.gxscript.data.entity;

import org.eadge.gxscript.tools.check.exception.NotMatchingInputOutputClasses;

import java.util.*;

/**
 * Created by eadgyo on 10/08/16.
 *
 * Default entity model
 */
public abstract class DefaultEntity extends Entity
{
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

    protected void addInputEntry(Class cl)
    {
        addInputEntry(getNumberOfInputs(), cl);
    }

    protected void addInputEntryNotNeeded(Class cl)
    {
        addInputEntryNotNeeded(getNumberOfInputs(), cl);
    }

    protected void addInputEntry(int inputIndex, Class cl)
    {
        addInputEntryNotNeeded(inputIndex, cl);
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
     * @param cl class of the input, void if it's just a link to another entity
     */
    protected void addInputEntryNotNeeded(int inputIndex, Class cl)
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

    protected void addOutputEntry(Class cl)
    {
        addOutputEntry(getNumberOfOutputs(), cl);
    }

    /**
     * Add not needed output entry at the given index
     * @param outputIndex output index
     * @param cl class of the output, void if it's just a link to another entity
     */
    protected void addOutputEntry(int outputIndex, Class cl)
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
            if (getInputClass(inputIndex) != entity.getOutputClass(entityOutput))
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
}
