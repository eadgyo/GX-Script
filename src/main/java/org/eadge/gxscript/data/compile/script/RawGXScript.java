package org.eadge.gxscript.data.compile.script;

import org.eadge.gxscript.data.entity.model.base.GXEntity;
import org.eadge.gxscript.data.entity.model.script.InputScriptGXEntity;
import org.eadge.gxscript.data.entity.model.script.OutputScriptGXEntity;
import org.eadge.gxscript.tools.Tools;

import java.io.Serializable;
import java.util.*;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Holds all entities in a GXScript
 */
public class RawGXScript implements Serializable
{
    /**
     * Keeps all entities
     */
    private List<GXEntity> entities = new ArrayList<>();

    /**
     * Keeps all entities with no inputs used
     */
    private List<GXEntity> startingEntities = new ArrayList<>();

    /**
     * Keeps all parameters entities
     */
    private List<InputScriptGXEntity> inputScriptGXEntities = new ArrayList<>();

    /**
     * Keeps all outputs entities
     */
    private List<OutputScriptGXEntity> outputScriptGXEntities = new ArrayList<>();

    public RawGXScript()
    {
    }

    public void clear()
    {
        entities.clear();
        startingEntities.clear();
        inputScriptGXEntities.clear();
        outputScriptGXEntities.clear();
    }

    public void addEntity(GXEntity GXEntity)
    {
        entities.add(GXEntity);
    }

    public void addAllEntities(Collection<GXEntity> entities)
    {
        this.entities.addAll(entities);
    }

    public void removeEntity(GXEntity gxEntity)
    {
        entities.remove(gxEntity);
        gxEntity.clearLinks();
    }

    public Collection<GXEntity> getEntities()
    {
        return entities;
    }

    public void setEntities(List<GXEntity> entities)
    {
        this.entities = entities;
    }

    public Collection<GXEntity> getStartingEntities()
    {
        return startingEntities;
    }

    public Collection<InputScriptGXEntity> getScriptInputEntities()
    {
        return inputScriptGXEntities;
    }

    public Collection<OutputScriptGXEntity> getScriptOutputEntities()
    {
        return outputScriptGXEntities;
    }

    public int numberOfEntities()
    {
        return entities.size();
    }

    public void updateEntities()
    {
        startingEntities.clear();
        inputScriptGXEntities.clear();
        outputScriptGXEntities.clear();

        startingEntities.addAll(Tools.getStartingEntities(entities));
        inputScriptGXEntities.addAll(Tools.getInputGXScriptEntities(startingEntities));
        outputScriptGXEntities.addAll(Tools.getOutputGXScriptEntities(entities));

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

        // Add not treated elements on script
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
