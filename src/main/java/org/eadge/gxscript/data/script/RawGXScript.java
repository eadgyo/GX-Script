package org.eadge.gxscript.data.script;

import org.eadge.gxscript.data.entity.Entity;
import org.eadge.gxscript.tools.Tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eadgyo on 03/08/16.
 *
 * Holds all entities in a GXScript
 */
public class RawGXScript
{
    private Set<Entity> entities = new HashSet<>();
    private Set<Entity> startingEntities = new HashSet<>();

    public RawGXScript()
    {
    }

    public void addEntity(Entity entity) { entities.add(entity); }

    public void removeEntity(Entity entity) { entities.remove(entity); }

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
}
