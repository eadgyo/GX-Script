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
     * Get all the input entities
     * @return all input entities
     */
    public abstract Collection<Entity> getAllInputEntities();

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
     * Get all the output entities
     * @return output entities keeping separated by output lane
     */
    public abstract Collection<Collection<Entity>> getAllOutputEntities();

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
}