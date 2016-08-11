package org.eadge.gxscript.tools;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.data.entity.imbrication.StartImbricationEntity;
import org.eadge.gxscript.data.imbrication.ImbricationNode;

import java.util.ArrayList;
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
            if (entity.getNumberOfInputs() == 0)
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
            if (entity.getNumberOfOutputs() == 0)
            {
                // This entity is a ending entity
                endingEntities.add(entity);
            }
        }

        return endingEntities;
    }

    /**
     * Get all entities in the imbrication of this start imbrication entity
     * @param startImbricationEntity used start imbrication entity
     * @param index imbrication index
     * @return all imbricated entities
     */
    public static Set<Entity> getInImbricationEntities(StartImbricationEntity startImbricationEntity, int index)
    {
        // Create set of imbricated entities
        Set<Entity> imbricatedEntities = new HashSet<>();

        // For each imbricated output
        Collection<Entity> imbricatedOutputs = startImbricationEntity.getImbricatedOutputs(index);
        for (Entity imbricatedOutput : imbricatedOutputs)
        {
            // Add their outputs recursively
            addInImbricationEntities(imbricatedOutput, imbricatedEntities);
        }

        return imbricatedEntities;
    }

    private static void addInImbricationEntities(Entity entity, Set<Entity> imbricatedEntities)
    {
        // Add this entity to all imbricated entities
        imbricatedEntities.add(entity);

        // Get outputs
        Collection<Entity> outputEntities = entity.getAllOutputEntitiesCollection();

        // Add all not treated outputs
        for (Entity outputEntity : outputEntities)
        {
            // If the output is not already treated
            if (!imbricatedEntities.contains(outputEntity))
            {
                addInImbricationEntities(outputEntity, imbricatedEntities);
            }
        }
    }

    public static void endImbrication(ImbricationNode root)
    {
        // Get all imbrication leaves
        ArrayList<ImbricationNode> leaves = root.getLeaves();

        // For all imbrications leaves
        for (ImbricationNode leave : leaves)
        {
            // If leaf have all entities processed at their level
            if (leave.hasFinishedProcess())
            {
                ImbricationNode parent = leave.getParent();
                if (parent != null)
                {
                    // End this imbrication
                    parent.endImbrication(leave);
                }
            }
        }
    }

    /**
     * Check if classes are equals or input class derived from output class
     * @param outputClass output class
     * @param inputClass input class
     * @return true if classes are equals or input class derived from output class
     */
    public static boolean isEqualOrDerivedFrom(Class outputClass, Class inputClass)
    {
        //noinspection unchecked
        return inputClass.isAssignableFrom(outputClass);
    }
}
