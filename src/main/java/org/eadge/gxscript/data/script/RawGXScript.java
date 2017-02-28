package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.entity.GXEntity;
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
    /**
     * Keeps all entities
     */
    private Set<GXEntity> entities = new HashSet<>();

    /**
     * Keeps all entities with no inputs used
     */
    private Set<GXEntity> startingEntities = new HashSet<>();

    public RawGXScript()
    {
    }

    public void addEntity(GXEntity GXEntity)
    {
        entities.add(GXEntity);
    }

    public void addAllEntities(Collection<GXEntity> entities)
    {
        this.entities.addAll(entities);
    }

    public void removeEntity(GXEntity GXEntity)
    {
        entities.remove(GXEntity);
    }

    public Collection<GXEntity> getEntities()
    {
        return entities;
    }

    public void setEntities(Set<GXEntity> entities)
    {
        this.entities = entities;
    }

    public Collection<GXEntity> getStartingEntities()
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
     * Add GXEntity and all linked entities in the same process recursively.
     *
     * @param GXEntity added GXEntity
     */
    public void addEntityRecursiveSearch(GXEntity GXEntity)
    {
        Set<GXEntity> alreadyTreated = new HashSet<>();
        Set<GXEntity> toBeTreated    = new HashSet<>();

        // Add the first element
        toBeTreated.add(GXEntity);

        // While there are still entities to be treated
        while (toBeTreated.size() != 0)
        {
            Iterator<GXEntity> iterator = toBeTreated.iterator();

            // Get the first element to be treated
            GXEntity next = iterator.next();

            // Remove this element from toBeDone and add it to already treated
            iterator.remove();
            alreadyTreated.add(next);

            treatEntityOutputs(next, alreadyTreated, toBeTreated);
            treatEntityInputs(next, alreadyTreated, toBeTreated);
        }

        // Add all treated elements
        addAllEntities(alreadyTreated);
    }

    private void treatEntityInputs(GXEntity GXEntity, Set<GXEntity> alreadyTreated, Set<GXEntity> toBeTreated)
    {
        // Get output entities
        Collection<GXEntity> inputsEntities = GXEntity.getAllInputEntities();

        // Add not treated elements on function
        addNotTreatedEntities(inputsEntities, alreadyTreated, toBeTreated);
    }

    private void treatEntityOutputs(GXEntity GXEntity, Set<GXEntity> alreadyTreated, Set<GXEntity> toBeTreated)
    {
        // Get output entities
        Collection<GXEntity> outputEntities = GXEntity.getAllOutputEntitiesCollection();

        // Add not treated elements on output
        addNotTreatedEntities(outputEntities, alreadyTreated, toBeTreated);
    }

    private void addNotTreatedEntities(Collection<GXEntity> testedEntities, Set<GXEntity> alreadyTreated, Set<GXEntity> toBeTreated)
    {
        // For all output entities not already treated
        for (GXEntity GXEntity : testedEntities)
        {
            // If the GXEntity is not treated
            if (GXEntity != null && !alreadyTreated.contains(GXEntity))
            {
                // If element is already in toBeDone entities, it will do nothing on toBeDone Set
                toBeTreated.add(GXEntity);
            }
        }
    }
}
