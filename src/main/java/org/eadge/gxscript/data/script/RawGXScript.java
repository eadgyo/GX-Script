package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.tools.Tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Holds all entities in a GXScript
 */
public class RawGXScript
{
    private Set<Entity> entities         = new HashSet<>();
    private Set<Entity> startingEntities = new HashSet<>();

    public RawGXScript()
    {
    }

    public void addEntity(Entity entity)
    {
        entities.add(entity);
    }

    public void addAllEntities(Collection<Entity> entities)
    {
        this.entities.addAll(entities);
    }

    public void removeEntity(Entity entity)
    {
        entities.remove(entity);
    }

    public Collection<Entity> getEntities()
    {
        return entities;
    }

    public void setEntities(Set<Entity> entities)
    {
        this.entities = entities;
    }

    public Collection<Entity> getStartingEntities()
    {
        return startingEntities;
    }

    public int numberOfEntities()
    {
        return entities.size();
    }

    public void updateStartingEntities()
    {
        startingEntities.clear();

        startingEntities.addAll(Tools.getStartingEntities(entities));
    }

    /**
     * Add entity and all linked entities in the same process recursively.
     *
     * @param entity added entity
     */
    public void addEntityRecursiveSearch(Entity entity)
    {
        Set<Entity> alreadyTreated = new HashSet<>();
        Set<Entity> toBeTreated    = new HashSet<>();

        // Add the first element
        toBeTreated.add(entity);

        // While there are still entities to be treated
        while (toBeTreated.size() != 0)
        {
            Iterator<Entity> iterator = toBeTreated.iterator();

            // Get the first element to be treated
            Entity next = iterator.next();

            // Remove this element from toBeDone and add it to already treated
            iterator.remove();
            alreadyTreated.add(next);

            treatEntityOutputs(next, alreadyTreated, toBeTreated);
            treatEntityInputs(next, alreadyTreated, toBeTreated);
        }

        // Add all treated elements
        addAllEntities(alreadyTreated);
    }

    private void treatEntityInputs(Entity entity, Set<Entity> alreadyTreated, Set<Entity> toBeTreated)
    {
        // Get output entities
        Collection<Entity> inputsEntities = entity.getAllInputEntities();

        // Add not treated elements on input
        addNotTreatedEntities(inputsEntities, alreadyTreated, toBeTreated);
    }

    private void treatEntityOutputs(Entity entity, Set<Entity> alreadyTreated, Set<Entity> toBeTreated)
    {
        // Get output entities
        Collection<Entity> outputEntities = entity.getAllOutputEntitiesCollection();

        // Add not treated elements on output
        addNotTreatedEntities(outputEntities, alreadyTreated, toBeTreated);
    }

    private void addNotTreatedEntities(Collection<Entity> testedEntities, Set<Entity> alreadyTreated, Set<Entity> toBeTreated)
    {
        // For all output entities not already treated
        for (Entity entity : testedEntities)
        {
            // If the entity is not treated
            if (entity != null && !alreadyTreated.contains(entity))
            {
                // If element is already in toBeDone entities, it will do nothing on toBeDone Set
                toBeTreated.add(entity);
            }
        }
    }
}
