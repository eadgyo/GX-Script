package org.eadge.gxscript.tools;

import org.eadge.gxscript.data.entity.Entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Tools shared with entity processes
 */
public class Tools
{
    /**
     * Get all starting entities from collection of entities. Start entities are those who have no entities linked in
     * input.
     *
     * @param entities collection of entities to be treated
     *
     * @return collection of starting entities
     */
    public static Collection<Entity> getStartingEntities(Collection<Entity> entities)
    {
        Set<Entity> startingEntities = new HashSet<>();

        for (Entity entity : entities)
        {
            // If entity has no linked entity block at input
            if (entity.getNumberOfInput() == 0)
            {
                // This entity is a starting entity
                startingEntities.add(entity);
            }
        }

        return startingEntities;
    }

    /**
     * Get all ending entities from collection of entities. Start entities are those who have no entities linked in
     * output.
     *
     * @param entities collection of entities to be treated
     *
     * @return collection of ending entities
     */
    public static Collection<Entity> getEndingEntities(Collection<Entity> entities)
    {
        Set<Entity> endingEntities = new HashSet<>();

        for (Entity entity : entities)
        {
            // If entity has no linked entity block at output
            if (entity.getNumberOfOutput() == 0)
            {
                // This entity is a ending entity
                endingEntities.add(entity);
            }
        }

        return endingEntities;
    }
}
