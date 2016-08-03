package org.eadge.gxscript.data.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 02/08/16.
 *
 * Parent Entity model
 *
 * Entity can have one entity per input and multiples entities per output
 */
public abstract class Entity
{
    /**
     * Indexes of all needed inputs
     */
    protected Set<Integer> indexesNeededInputs = new HashSet<>();

    //----------------------------
    //---------- Input -----------
    //----------------------------

    /**
     * Get the class of the input object at the index
     * @param index input index
     * @return class of the input object at the index
     */
    public abstract Class getInputClass(int index);

    /**
     * Get all the classes of the input objects
     * @return classes of the input objects
     */
    public abstract Collection<Class> getAllInputClasses();

    /**
     * Get the number of input objects
     * @return number of input objects
     */
    public abstract int getNumberOfInput();

    /**
     * Get the input entity at the index or null if there are no entities
     * @param index input index
     * @return input entity at the index or null if there are no entities
     */
    public abstract Entity getInputEntity(int index);

    /**
     * Get the index of the input linked to one entity or -1 if it's not a input entity
     * @param entity linked to input entity
     * @return index of the linked to input entity, or -1 if entity is not link on input
     */
    public abstract int getIndexOfInputEntity(Entity entity);

    /**
     * Get the index of the output linked to one entity or -1 if it's not a output entity
     * @param entity linked to output entity
     * @return index of the linked to output entity, or -1 if entity is not link on output
     */
    public abstract int getIndexOfOutputEntityOnInput(int index, Entity entity);

    /**
     * Get all the input entities
     * @return all input entities
     */
    public abstract Collection<Entity> getAllInputEntities();

    /**
     * Check if entity have necessary input
     * @return true if entity have enough input, false otherwise
     */
    public boolean hasAllNeededInput()
    {
        // Check if all needed input are linked
        for (Integer indexNeededInput : indexesNeededInputs)
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

    /**
     * Check if the input at the index is valid
     * @param index input index
     * @return true if the input is valid, false otherwise
     */
    public boolean isValidInput(int index)
    {
        Entity inputEntity = getInputEntity(index);

        // If input is not linked
        if (inputEntity == null)
            return true;

        // Get his linked index
        int indexOfOutputEntity = getIndexOfOutputEntityOnInput(index, inputEntity);

        // If they are partially linked
        if (indexOfOutputEntity == -1)
        {
            // There is a problem
            return false;
        }

        // If they have no matching intput/output types
        if (getInputClass(index) != inputEntity.getInputClass(indexOfOutputEntity))
        {
            // Types are not matching
            return false;
        }

        return true;
    }

    /**
     * Check if all the inputs of are valid
     * @return true if inputs are valid, false otherwise
     */
    public boolean hasValidInput()
    {
        // Check for all inputs
        for (int index = 0; index < getNumberOfInput(); index++)
        {
            // If the input is not valid
            if (!isValidInput(index))
            {
                return false;
            }
        }
        return true;
    }

    //----------------------------
    //----------- Output ---------
    //----------------------------

    /**
     * Get the class of the output object at the index
     * @param index output index
     * @return class of the output object at the index
     */
    public abstract Class getOutputClass(int index);

    /**
     * Get all the classes of the output objects
     * @return classes of the output objects in collection
     */
    public abstract Collection<Class> getAllOutputClasses();

    /**
     * Get the number of output objects
     * @return number of output objects
     */
    public abstract int getNumberOfOutput();

    /**
     * Get the output entities at the index
     * @param index output index
     * @return output entities at the index
     */
    public abstract Collection<Entity> getOutputEntities(int index);

    /**
     * Get the index of the output linked to one entity or -1 if it's not a output entity
     * @param entity linked to output entity
     * @return index of the linked to output entity, or -1 if entity is not link on output
     */
    public abstract int getIndexOfOutputEntity(Entity entity);

    /**
     * Get the index of the input linked to one entity or -1 if it's not a input entity
     * @param index entity output index
     * @param entity linked to input entity
     * @return index of the linked to input entity, or -1 if entity is not link on input
     */
    public abstract int getIndexOfInputEntityOnOutput(int index, Entity entity);

    /**
     * Get all the output entities
     * @return output entities keeping separated by output lane
     */
    public abstract Collection<Collection<Entity>> getAllOutputEntities();

    /**
     * Get all the output entities
     * @return output entities
     */
    public Collection<Entity> getAllOutputEntitiesCollection()
    {
        Collection<Entity> allOutputEntities = new ArrayList<>();
        Collection<Collection<Entity>> allOutputEntitiesSeparated = getAllOutputEntities();

        for (Collection<Entity> entities : allOutputEntitiesSeparated)
        {
            allOutputEntities.addAll(entities);
        }
        return allOutputEntities;
    }

    /**
     * Check if the output at the index is valid
     * @param index output index
     * @return true if the output is valid, false otherwise
     */
    public boolean isValidOutput(int index)
    {
        Collection<Entity> outputEntities = getOutputEntities(index);

        // For all connected blocks
        for (Entity outputEntity : outputEntities)
        {
            // Get his linked index
            int indexOfInputEntity = getIndexOfInputEntityOnOutput(index, outputEntity);

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
     * @return true if outputs are valid, false otherwise
     */
    public boolean hasValidOutput()
    {
        // Check for all outputs
        for (int index = 0; index < getNumberOfOutput(); index++)
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