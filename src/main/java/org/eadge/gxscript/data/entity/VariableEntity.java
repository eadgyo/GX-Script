package org.eadge.gxscript.data.entity;

import java.util.Collection;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Variable entity model
 * Variable have input and output as entity, but also have modified lane
 */
public abstract class  VariableEntity extends Entity
{
    //----------------------------
    //-------- Modified ----------
    //----------------------------

    /**
     * Get the class of the modified object at the index
     * @param index input index
     * @return class of the modified object at the index
     */
    public abstract Class getModifiedClass(int index);

    /**
     * Get all the classes of the modified objects
     * @return classes of the modified objects
     */
    public abstract Collection<Class> getAllModifiedClasses();

    /**
     * Get the number of modified objects
     * @return number of modified objects
     */
    public abstract int getNumberOfModified();

    /**
     * Get the modified linked entities at the index
     * @param index modified index
     * @return modified linked entity at the index
     */
    public abstract Collection<Entity> getModifiedEntities(int index);

    /**
     * Get all the modified entities
     * @return all modified entities keeping separated by modified lane
     */
    public abstract Collection<Entity> getAllInputEntities();
}
